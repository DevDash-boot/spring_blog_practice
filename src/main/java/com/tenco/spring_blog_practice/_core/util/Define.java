package com.tenco.spring_blog_practice._core.util;

public class Define {
    // session user상수 지정
    public static final String SESSION_USER = "sessionUser";
    // 이메일 인증
    public static final String EMAIL_CODE_PREFIX = "emailCode_";    // emailCode_a@naver.com
    public static final String VERIFIED_EMAIL = "verified";         // 인증을 마친 이메일 주소
    public static final int CODE_EXPIRE_MINUTES = 1;    // 인증 번호 유효 시간
}
