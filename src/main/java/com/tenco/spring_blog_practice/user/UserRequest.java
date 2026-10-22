package com.tenco.spring_blog_practice.user;

import com.tenco.spring_blog_practice._core.error.Exception400;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class UserRequest {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{4,20}$");

    @Data
    public static class joinDto {
        private String username;
        private String password;
        private String email;
        private MultipartFile profileImage; // 프로필 이미지(선택 사항)

        public void validate() {
            if (username == null || username.isEmpty()) {
                throw new Exception400("사용자명은 필수입니다.");
            }
            if (!USERNAME_PATTERN.matcher(username).matches()) {
                throw new Exception400("사용자명은 영문, 숫자, 밑줄(_) 4~20자로 입력해주세요.");
            }
            // 소셜 로그인 사용자명(kakao_123 등)을 미리 선점하는 것 방지
            if (username.startsWith("kakao_") || username.startsWith("google_") || username.startsWith("naver_")) {
                throw new Exception400("사용할 수 없는 사용자명입니다.");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new Exception400("비밀번호는 필수입니다.");
            }
            if (password.length() < 4) {
                throw new Exception400("비밀번호는 4글자 이상입니다.");
            }
            // BCrypt는 72바이트까지만 처리한다.
            if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
                throw new Exception400("비밀번호가 너무 깁니다.");
            }
            if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
                throw new Exception400("올바른 이메일 형식이 아닙니다.");
            }
        }


        public User toEntity(String profileImageFilename, String encodePassword) {
            return User.builder()
                    .username(username)
                    .password(encodePassword)
                    .email(email)
                    .profileImage(profileImageFilename)
                    .build();
        }
    }

    @Data
    public static class loginDto {
        private String username;
        private String password;

        public void validate() {
            if (username == null || username.trim().isEmpty()) {
                throw new Exception400("사용자명은 필수입니다.");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new Exception400("비밀번호는 필수입니다.");
            }
        }
    }

    @Data
    public static class updateDto {
        private String password;
        private MultipartFile profileImage; // 새 프로필 이미지(선택 사항)

        // 비밀번호를 입력했는지 확인(null, 공백 입력 -> 입력 안함)
        public boolean hasPassword(){
            return password != null && !password.isBlank(); // 비밀번호 입력시 true 반환
        }

        // 새 프로필 사진을 골랐는지
        public boolean hasProfileImage(){
            return profileImage != null && !profileImage.isEmpty(); // 새 파일 선택 시 true 반환
        }

        public void validate() {
            if(!hasPassword() && !hasProfileImage()){   // 둘 다 입력 안함
                throw new Exception400("비밀번호 또는 프로필 이미지를 변경해주세요.");
            }

            // 필요하다면 비밀번호 길이 제한, 특수문자 포함 여부 활용 가능
            if (hasPassword() && password.length() < 4) {
                throw new Exception400("비밀번호는 4글자 이상이어야 합니다.");
            }
        }
    }

    // 이메일 인증 요청 DTO(발송, 확인 API와 함께 사용)
    @Data
    public static class emailCheckDto {
        private String email;   // 이메일 주소(1번째 요청 시 사용)
        private String code;    // 인증 번호(2번째 요청 시 사용)

        public void validate() {
            if (email == null || email.trim().isEmpty()) {
                throw new Exception400("이메일을 입력해주세요.");
            }
            if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
                throw new Exception400("올바른 이메일 형식이 아닙니다.");
            }
        }

        // 인증 번호 확인. API에서 추가로 호출할 예정
        public void validateCode() {
            validate();
            if (code == null || code.trim().isEmpty()) {
                throw new Exception400("인증 번호를 입력해주세요.");
            }
            if (!code.matches("\\d{6}")) {
                throw new Exception400("인증 번호 6자리를 입력해주세요.");
            }
        }
    }
}
