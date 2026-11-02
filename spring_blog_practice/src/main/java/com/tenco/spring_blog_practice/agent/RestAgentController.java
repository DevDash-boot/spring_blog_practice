package com.tenco.spring_blog_practice.agent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tenco.spring_blog_practice._core.util.Define;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * 게시글 내용을 Gemini(AI)에게 보내 맞춤법과 띄어쓰기를 교정받는 API
 * - 글쓰기 / 글수정 화면의 [AI 교정] 버튼이 이 주소를 호출한다.
 * - API 키는 .env 의 GEMINI_API_KEY 로 관리한다.(코드나 깃허브에 올리지 않는다.)
 */
@Slf4j
@RestController // @Controller + @ResponseBody
public class RestAgentController {
    // 요청할 외부 API 주소(모델 이름은 application.yaml 의 agent.gemini.model 로 변경 가능)
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent";
    // 게시글 본문 최대 길이(BoardRequest 의 검증 기준과 동일)
    private static final int MAX_CONTENT_LENGTH = 10000;

    @Value("${agent.gemini.api-key:}")
    private String apiKey;

    @Value("${agent.gemini.model:gemini-3.5-flash-lite}")
    private String model;

    private final RestTemplate restTemplate = createRestTemplate();

    // 제미나이에게 묻는다. 이 내용을 교정해달라고.
    // POST http://localhost:8080/api/agent/proofread   (form 데이터 : content)
    @PostMapping("/api/agent/proofread")
    public ResponseEntity<String> proofread(
            @RequestParam(name = "content", required = false) String content,
            HttpSession session) {

        // 1. 로그인 확인 : 외부 API 사용량(비용) 보호를 위해 로그인 한 사용자만 사용할 수 있다.
        if (session.getAttribute(Define.SESSION_USER) == null) {
            return text(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        // 2. 입력값 확인
        if (content == null || content.isBlank()) {
            return text(HttpStatus.BAD_REQUEST, "교정할 내용을 입력해주세요.");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            return text(HttpStatus.BAD_REQUEST, "내용은 " + MAX_CONTENT_LENGTH + "자 이하로 입력해주세요.");
        }
        // 3. API 키 설정 확인(.env 에 GEMINI_API_KEY 가 없으면 빈 값)
        if (apiKey == null || apiKey.isBlank()) {
            log.error("GEMINI_API_KEY 가 설정되지 않았습니다. .env 파일을 확인해주세요.");
            return text(HttpStatus.SERVICE_UNAVAILABLE, "AI 교정 기능이 설정되지 않았습니다.");
        }

        // 요청 헤더 작성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        // 요청 바디 작성
        ProofreadRequestDto requestDto = new ProofreadRequestDto(
                List.of(new ProofreadRequestDto.Content(
                        List.of(new ProofreadRequestDto.Content.Part(
                                "다음 블로그 내용의 맞춤법과 띄어쓰기를 교정해 주세요. 몇 줄 띄워서 간단한 농담도 던져주세요.\n\n"
                                        + content
                        ))
                ))
        );

        // 바디, 헤더의 HttpEntity 제작
        HttpEntity<ProofreadRequestDto> httpEntity = new HttpEntity<>(requestDto, headers);

        try {
            // 요청 보내기 - exchange
            ResponseEntity<ProofreadResponseDto> response = restTemplate.exchange(
                    GEMINI_URL.formatted(model),
                    HttpMethod.POST,
                    httpEntity,
                    ProofreadResponseDto.class
            );

            String proofread = extractText(response.getBody());
            if (proofread == null) {
                // 안전 필터 등으로 후보 응답이 비어 있는 경우
                log.warn("Gemini 응답에 교정 결과가 없습니다.");
                return text(HttpStatus.BAD_GATEWAY, "AI가 교정 결과를 만들지 못했습니다. 내용을 바꿔서 다시 시도해주세요.");
            }
            // 사용자 글 내용은 개인정보가 될 수 있어 길이만 로그로 남긴다.
            log.info("AI 교정 완료 - 교정 전 {}자, 교정 후 {}자", content.length(), proofread.length());
            return text(HttpStatus.OK, proofread);

        } catch (RestClientResponseException e) {
            // 4xx, 5xx : API 키 오류, 사용량 초과(429), 모델 이름 오류 등
            log.warn("Gemini API 오류 - 상태 : {}, 응답 : {}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode().value() == 429) {
                return text(HttpStatus.TOO_MANY_REQUESTS, "요청이 많아 잠시 후 다시 시도해주세요.");
            }
            return text(HttpStatus.BAD_GATEWAY, "외부 API 요청에 실패했습니다.");
        } catch (RestClientException e) {
            // 연결 실패, 시간 초과 등 응답을 받지 못한 경우
            log.warn("Gemini 서버 연결 실패 : {}", e.getMessage());
            return text(HttpStatus.GATEWAY_TIMEOUT, "AI 서버에 연결할 수 없습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    // 응답에서 교정된 텍스트만 꺼낸다. 없으면 null
    private String extractText(ProofreadResponseDto body) {
        if (body == null || body.candidates() == null || body.candidates().isEmpty()) {
            return null;
        }
        ProofreadResponseDto.Candidate candidate = body.candidates().getFirst();
        if (candidate == null || candidate.content() == null || candidate.content().parts() == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (ProofreadResponseDto.Candidate.Content.Part part : candidate.content().parts()) {
            if (part != null && part.text() != null) {
                sb.append(part.text());
            }
        }
        return sb.isEmpty() ? null : sb.toString();
    }

    // 화면(fetch)에서 response.text() 로 읽기 때문에 한글이 깨지지 않도록 UTF-8 텍스트로 응답
    private ResponseEntity<String> text(HttpStatus status, String body) {
        return ResponseEntity.status(status)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(body);
    }

    // 외부 서버가 응답하지 않아도 우리 서버가 무한정 기다리지 않도록 시간 제한
    private static RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));   // 서버 연결 대기 최대 5초
        factory.setReadTimeout(Duration.ofSeconds(30));     // AI 응답 대기 최대 30초
        return new RestTemplate(factory);
    }

    // ===== Gemini 요청/응답 DTO (JSON 구조에 맞춘 record) =====
    public record ProofreadRequestDto(
            List<Content> contents
    ) {
        public record Content(
                List<Part> parts
        ) {
            public record Part(String text) {
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProofreadResponseDto(
            List<Candidate> candidates
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Candidate(
                Content content
        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            public record Content(
                    List<Part> parts
            ) {
                @JsonIgnoreProperties(ignoreUnknown = true)
                public record Part(
                        String text
                ) {
                }
            }
        }
    }
}
