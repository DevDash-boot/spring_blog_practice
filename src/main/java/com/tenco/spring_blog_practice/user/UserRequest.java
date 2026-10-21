package com.tenco.spring_blog_practice.user;

import com.tenco.spring_blog_practice._core.error.Exception400;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class UserRequest {
    @Data
    public static class joinDto {
        private String username;
        private String password;
        private String email;
        private MultipartFile profileImage; // 프로필 이미지(선택 사항)

        public void validate() {
            if (username == null || username.trim().isEmpty()) {
                throw new Exception400("사용자명은 필수입니다.");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new Exception400("비밀번호는 필수입니다.");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new Exception400("이메은 필수입니다.");
            }
            if (password.length() < 4) {
                throw new Exception400("비밀번호는 4글자 이상입니다.");
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
}
