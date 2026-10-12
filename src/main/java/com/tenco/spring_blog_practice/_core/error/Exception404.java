package com.tenco.spring_blog_practice._core.error;

public class Exception404 extends RuntimeException {
    public Exception404(String message) {
        super(message);
    }

    public static void main(String[] args) {
        new Exception404("페이지를 찾을 수 없습니다..");
    }
}