package com.tenco.spring_blog_practice._core.error;

public class Exception401 extends RuntimeException {
    public Exception401(String message) {
        super(message);
    }

    public static void main(String[] args) {
        new Exception401("인증이 필요합니다.");
    }
}