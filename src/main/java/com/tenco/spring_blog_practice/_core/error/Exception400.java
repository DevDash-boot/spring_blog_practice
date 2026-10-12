package com.tenco.spring_blog_practice._core.error;

public class Exception400 extends RuntimeException {
    public Exception400(String message) {
        super(message);
    }

    public static void main(String[] args) {
        new Exception400("필수 입력 항목이 누락되었습니다..");
    }
}