package com.tenco.spring_blog_practice.user;

import com.tenco.spring_blog_practice._core.error.Exception400;
import lombok.Data;

public class UserRequest {
    @Data
    public static class joinDto {
        private String username;
        private String password;
        private String email;

        public void validate() {
            if (username == null || username.trim().isEmpty()) {
                throw new Exception400("사용자명은 필수입니다.");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new Exception400("비밀번호는 필수입니다.");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new Exception400("이름은 필수입니다.");
            }
            if (password.length() < 4) {
                throw new Exception400("비밀번호는 4글자 이상입니다.");
            }
        }

        public User toEntity() {
            return User.builder().username(username).password(password).email(email).build();
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

        public void validate() {
            if (password == null || password.trim().isEmpty()) {
                throw new Exception400("비밀번호는 필수입니다.");
            }
        }
    }
}
