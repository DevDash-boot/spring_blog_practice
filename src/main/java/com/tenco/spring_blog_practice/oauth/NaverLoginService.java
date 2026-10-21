package com.tenco.spring_blog_practice.oauth;

import com.tenco.spring_blog_practice._core.error.UnauthorizedException;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import com.tenco.spring_blog_practice.user.UserResponse;
import com.tenco.spring_blog_practice.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

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
            throw new UnauthorizedException("네이버 로그인이 취소되었습니다.");
        }
        try {
            // 1. 인가 코드와 state로 액세스 토큰 발급
            UserResponse.OauthToken oAuthToken = naverApiClient.requestAccessToken(code, state);
            // 2. 액세스 토큰으로 사용자 정보 조회
            UserResponse.NaverUserInfo naverUserInfo = naverApiClient.requestProfile(oAuthToken.getAccessToken());
            // 3. 자동 회원 가입 또는 로그인
            return userService.naverLogin(naverUserInfo);
        } catch (HttpClientErrorException e) {
            log.warn("네이버 API 오류 : {}", e.getResponseBodyAsString());
            throw new UnauthorizedException("네이버 로그인에 실패했습니다. 다시 시도해주세요.");
        } catch (ResourceAccessException e) {
            log.warn("네이버 서버에 연결 실패 : {}", e.getMessage());
            throw new UnauthorizedException("네이버 서버에 연결할 수 없습니다. 잠시 후 다시 시도해 주세요.");
        }
    }
}
