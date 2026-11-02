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

// https://developers.naver.com/docs/login/api/api.md

@Slf4j
@Component
public class NaverApiClient {
    // application.yaml의 변수를 가지고 올 때 사용
    @Value("${oauth.naver.client-id}")
    private String naverClientId;
    @Value("${oauth.naver.client-secret}")
    private String naverClientSecret;
    @Value("${oauth.naver.redirect-uri}")
    private String naverRedirectUri;

    private static final String AUTHORIZE_URL = "https://nid.naver.com/oauth2.0/authorize";  // 인가 요청 주소
    private static final String TOKEN_URL = "https://nid.naver.com/oauth2.0/token";  // 액세스 토큰 요청 주소
    private static final String PROFILE_URL = "https://openapi.naver.com/v1/nid/me";  // 프로필 이미지 요청 주소
    private final RestTemplate restTemplate = createRestTemplate();  // 한 번 만들면 여러 요청에서 함께 사용해도 안전하게 설계되어 하나만 만들어 재사용한다.

    // 1. 네이버 로그인 화면 주소 만들기(로그인 + 동의 화면)
    public String getAuthorizeUrl(String state){
        return AUTHORIZE_URL
                + "?client_id=" + naverClientId
                + "&redirect_uri=" + naverRedirectUri
                + "&state=" + state
                + "&response_type=code";
    }

    // 2. 인가 코드로 액세스 토큰 요청(POST, 폼 데이터)
    public UserResponse.OauthToken requestAccessToken(String code, String state){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> Params = new LinkedMultiValueMap<>();
        Params.add("grant_type", "authorization_code");
        Params.add("client_id", naverClientId);
        Params.add("redirect_uri", naverRedirectUri);
        Params.add("code", code);
        Params.add("client_secret", naverClientSecret);    // 클라이언트 시크릿 기능이 활성화 되면 필수
        Params.add("state", state);

        // HTTP 요청 메세지 : 바디 + 헤더
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity(Params, headers);

        // exchange(주소, method, HTTP 메세지, 응답 타입)
        ResponseEntity<UserResponse.OauthToken> response = restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                tokenRequest,
                UserResponse.OauthToken.class
        );
        log.info("네이버 액세스 토큰 발급 완료");   // 토큰 값은 로그에 남기지 않는다.
        return response.getBody();
    }

    // 3. 액세스 토큰으로 사용자 정보 요청(GET, 인증 헤더)
    public UserResponse.NaverUserInfo requestProfile(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<UserResponse.NaverUserInfo> response = restTemplate.exchange(
                PROFILE_URL,
                HttpMethod.GET,
                request,
                UserResponse.NaverUserInfo.class
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
