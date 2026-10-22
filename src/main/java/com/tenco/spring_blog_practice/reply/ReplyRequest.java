package com.tenco.spring_blog_practice.reply;

import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice.board.Board;
import com.tenco.spring_blog_practice.user.User;
import lombok.Data;

public class ReplyRequest {
    @Data
    public static class saveDto {
        private String content;

        public void validate() {
            if (content == null || content.trim().isEmpty()) {
                throw new Exception400("내용은 필수입니다.");
            }
            if (content.trim().length() > 255) {
                throw new Exception400("댓글은 255자 이하로 입력해주세요.");
            }
        }

        public Reply toEntity(User user, Board board) {
            Reply reply = Reply.builder()
                    .content(content.trim())
                    .build();

            reply.setUser(user);
            reply.setBoard(board);
            return reply;
        }
    }

    @Data
    public static class updateDto {
        private String content;

        public void validate() {
            if (content == null || content.trim().isEmpty()) {
                throw new Exception400("내용은 필수입니다.");
            }
            if (content.trim().length() > 255) {
                throw new Exception400("댓글은 255자 이하로 입력해주세요.");
            }
        }
    }
}
