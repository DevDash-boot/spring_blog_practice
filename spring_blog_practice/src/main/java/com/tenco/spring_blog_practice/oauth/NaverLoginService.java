package com.tenco.spring_blog_practice.oauth;

import com.tenco.spring_blog_practice._core.error.Exception401;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import com.tenco.spring_blog_practice.user.UserResponse;
import com.tenco.spring_blog_practice.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverLoginService {
    private final NaverApiClient naverApiClient;
    private final UserService userService;

    // 네이버 로그인 화면 주소
    public String getAuthorizeUrl(String state) {
        return naverApiClient.getAuthorizeUrl(state);
    }

    // 네이버 로그인 : 인가 코드 -> 토큰 -> 사용자 정보 -> 가입 또는 로그인
    public SessionUserDto login(String code, String state) {
        if (code == null) {
            throw new Exception401("네이버 로그인이 취소되었습니다.");
        }
        try {
            UserResponse.OauthToken token = naverApiClient.requestAccessToken(code, state);
            if (token == null || token.getAccessToken() == null) {
                throw new Exception401("네이버 로그인에 실패했습니다. 다시 시도해주세요.");
            }
            UserResponse.NaverUserInfo info = naverApiClient.requestProfile(token.getAccessToken());
            if (info == null || info.getResponse() == null || info.getResponse().getId() == null) {
                throw new Exception401("네이버 사용자 정보를 가져오지 못했습니다.");
            }
            return userService.naverLogin(info);
        } catch (RestClientException e) {
            log.warn("네이버 통신 오류 : {}", e.getMessage());
            throw new Exception401("네이버 로그인 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }
}
