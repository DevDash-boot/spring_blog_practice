package com.tenco.spring_blog_practice.user;

import lombok.Getter;

@Getter
public class SessionUserDto {
    private Long id;
    private String username;
    private String email;

    public SessionUserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
