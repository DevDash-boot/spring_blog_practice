package com.tenco.spring_blog_practice._core.error;

public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }

    public static void main(String[] args) {
        new Exception500("서버 내부 에러.");
    }
}