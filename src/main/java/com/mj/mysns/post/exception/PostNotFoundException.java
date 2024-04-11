package com.mj.mysns.post.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("post 를 찾을 수 없습니다");
    }

    public PostNotFoundException(String message) {
        super(message);
    }

}
