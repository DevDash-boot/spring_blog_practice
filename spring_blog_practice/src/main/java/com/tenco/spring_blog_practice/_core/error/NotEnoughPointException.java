package com.tenco.spring_blog_practice._core.error;

public class NotEnoughPointException extends RuntimeException{
    public NotEnoughPointException(String msg) {
        super(msg);
    }
}
