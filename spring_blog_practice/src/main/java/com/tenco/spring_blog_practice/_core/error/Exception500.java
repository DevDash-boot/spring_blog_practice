package com.tenco.spring_blog_practice._core.error;

public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }
}