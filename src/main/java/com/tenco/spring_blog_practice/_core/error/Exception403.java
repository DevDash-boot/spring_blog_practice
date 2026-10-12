package com.tenco.spring_blog_practice._core.error;

public class Exception403 extends RuntimeException {
    public Exception403(String message) {
        super(message);
    }

    public static void main(String[] args) {
        new Exception403("권한이 없습니다.");
    }
}