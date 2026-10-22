package com.tenco.spring_blog_practice.oauth;

import com.tenco.spring_blog_practice.user.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import java.time.Duration;

// 구글 https://developers.google.com/identity/openid-connect/reference?hl=ko&utm_source=chatgpt.com

@Slf4j
@Component
public class GoogleApiClient {
    @Value("${oauth.google.client-id}")
    private String googleClientId;
    @Value("${oauth.google.client-secret}")
    private String googleClientSecret;
    @Value("${oauth.google.redirect-uri}")
    private String googleRedirectUri;

    private static final String AUTHORIZE_URL = "https://accounts.google.com/o/oauth2/v2/auth";  // 인가 요청 주소
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";  // 액세스 토큰 요청 주소
    private static final String PROFILE_URL = "https://openidconnect.googleapis.com/v1/userinfo";  // 프로필 이미지 요청 주소
    private final RestTemplate restTemplate = createRestTemplate();   // 한 번 만들면 여러 요청에서 함께 사용해도 안전하게 설계되어 하나만 만들어 재사용한다.

    // 1. 구글 로그인 화면 주소 만들기(로그인 + 동의 화면)
    public String getAuthorizeUrl() {
        return org.springframework.web.util.UriComponentsBuilder
                .fromUriString(AUTHORIZE_URL)
                .queryParam("client_id", googleClientId)
                .queryParam("redirect_uri", googleRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email profile")
                .build()
                .encode()
                .toUriString();
    }

    // 2. 인가 코드로 액세스 토큰 요청(POST, 폼 데이터)
    public UserResponse.OauthToken requestAccessToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> Params = new LinkedMultiValueMap<>();
        Params.add("grant_type", "authorization_code");
        Params.add("client_id", googleClientId);
        Params.add("redirect_uri", googleRedirectUri);
        Params.add("code", code);
        Params.add("client_secret", googleClientSecret);    // 클라이언트 시크릿 기능이 활성화 되면 필수

        // HTTP 요청 메세지 : 바디 + 헤더
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity(Params, headers);

        // exchange(주소, method, HTTP 메세지, 응답 타입)
        ResponseEntity<UserResponse.OauthToken> response = restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                tokenRequest,
                UserResponse.OauthToken.class
        );
        log.info("구글 액세스 토큰 발급 완료");   // 토큰 값은 로그에 남기지 않는다.
        return response.getBody();
    }

    // 3. 액세스 토큰으로 사용자 정보 요청(GET, 인증 헤더)
    public UserResponse.GoogleUserInfo requestProfile(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<UserResponse.GoogleUserInfo> response = restTemplate.exchange(
                PROFILE_URL,
                HttpMethod.GET,
                request,
                UserResponse.GoogleUserInfo.class
        );
        return response.getBody();
    }

    private static RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));   // 서버 연결 대기 최대 5초
        factory.setReadTimeout(Duration.ofSeconds(5));      // 응답 대기 최대 5초
        return new RestTemplate(factory);
    }
}
