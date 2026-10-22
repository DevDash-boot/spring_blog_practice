package com.tenco.spring_blog_practice.board;

import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice.user.User;
import lombok.Data;

public class BoardRequest {
    @Data
    public static class saveDto {
        private String title;
        private String content;

        public void validate() {
            if (title == null || title.trim().isEmpty()) {
                throw new Exception400("제목은 필수입니다.");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new Exception400("내용은 필수입니다.");
            }
            if (title.trim().length() > 100) {
                throw new Exception400("제목은 100자 이하로 입력해주세요.");
            }
            if (content.length() > 10000) {   // MySQL TEXT는 65,535바이트(한글 약 2만 자)
                throw new Exception400("내용은 10,000자 이하로 입력해주세요.");
            }
        }

        public Board toEntity(User user) {
            return Board.builder().title(title).content(content).user(user).build();
        }
    }

    @Data
    public static class updateDto {
        private String title;
        private String content;

        public void validate() {
            if (title == null || title.trim().isEmpty()) {
                throw new Exception400("제목은 필수입니다.");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new Exception400("내용은 필수입니다.");
            }
            if (title.trim().length() > 100) {
                throw new Exception400("제목은 100자 이하로 입력해주세요.");
            }
            if (content.length() > 10000) {   // MySQL TEXT는 65,535바이트(한글 약 2만 자)
                throw new Exception400("내용은 10,000자 이하로 입력해주세요.");
            }
        }
    }
}
