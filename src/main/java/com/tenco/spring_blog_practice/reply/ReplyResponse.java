package com.tenco.spring_blog_practice.reply;

import lombok.Data;
@Data
public class ReplyResponse {
    private Long id;
    private String content;
    private String username;
    private boolean isOwner;

    public ReplyResponse(Reply reply, Long sessionUserId) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.username = reply.getUser().getUsername();
        this.isOwner = sessionUserId != null
                && reply.isOwner(sessionUserId);
    }
}