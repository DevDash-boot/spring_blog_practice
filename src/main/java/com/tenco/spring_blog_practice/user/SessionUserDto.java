package com.tenco.spring_blog_practice.user;

import lombok.Getter;

@Getter
public class SessionUserDto {
    private Long id;
    private String username;
    private String email;
    private boolean admin;  // 관리자 여부
    private String roleDisplay; // 화면 표시용(ADMIN 또는 USER)

    public SessionUserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.admin = user.isAdmin();    // 관리자이면 true
        this.roleDisplay = this.admin ? "ADMIN" : "USER";
    }
}
