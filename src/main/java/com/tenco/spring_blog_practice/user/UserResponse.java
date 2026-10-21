package com.tenco.spring_blog_practice.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tenco.spring_blog_practice._core.util.MyDataUtil;
import lombok.Data;

public class UserResponse {
    // 마이페이지 응답 DTO
    @Data
    public static class DetailDto {
        private Long id;
        private String username;
        private String email;
        private String profileImage; // 파일 이름(없으면 null)
        private String createdAt;   // 포맷된 가입일

        public DetailDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.profileImage = user.getProfilePath();
            this.createdAt =  MyDataUtil.timestampFormat(user.getCreatedAt());
        }
    }

    // 회원가입 응답 DTO
    @Data
    public static class JoinDto {
        private Long id;
        private String username;
        private String email;

        public JoinDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }

    // 회원정보 수정 화면 응답 DTO
    @Data
    public static class updateFormDto {
        private Long id;
        private String username;
        private String email;
        private String profileImage;

        public updateFormDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.profileImage = user.getProfilePath();
        }
    }

    // 카카오 액세스 토큰 응답 DTO
    @Data
    public static class OauthToken {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("expires_in")
        private Long expiresIn;
        @JsonProperty("scope")
        private String scope;
        @JsonProperty("refresh_token_expires_in")
        private Long refreshTokenExpiresIn;
    }

    // 카카오 소셜 로그인 사용자 정보 DTO
    @Data
    public static class KakaoUserInfo {
        private Long id;
        @JsonProperty("connected_at")
        private String connectedAt;
        private Properties properties;
        @JsonProperty("kakao_account")
        private Object kakaoAccount;
        @Data
        public static class Properties {
            private String nickname;
            @JsonProperty("profile_image")
            private String profileImage;
            @JsonProperty("thumbnail_image")
            private String thumbnailImage;
        }
    }

    // 구글은 사용자 정보를 반환하는 JSON 구조가 카카오와 다르다.
    // 구글 문서에서 UserInfo 엔드포인트 확인해보면 sub, name, email, picture, given_name, family_name 형태이다.
    @Data
    public static class GoogleUserInfo {
        private String sub; // 구글은 id가 아닌 sub 사용
        private String name;
        private String email;
        private String picture;
    }

    // 네이버 액세스 토큰 응답 DTO
    @Data
    public static class NaverOauthToken {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("expires_in")
        private Long expiresIn;
        @JsonProperty("error")
        private String error;
        @JsonProperty("error_description")
        private String errorDescription;
    }

    // 네이버 사용자 정보 응답 DTO
    @Data
    public static class NaverUserInfo {
        private String resultcode;
        private String message;
        private Response response;

        @Data
        public static class Response {
            private String id;
            private String nickname;
            private String name;
            private String email;

            @JsonProperty("profile_image")
            private String profileImage;
        }
    }
}
