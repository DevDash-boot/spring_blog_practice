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
                throw new Exception400("내용 필수입니다.");
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
                throw new Exception400("내용 필수입니다.");
            }
        }
    }
}
