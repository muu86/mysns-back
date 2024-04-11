package com.mj.mysns.post.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException() {
        super("comment 를 찾을 수 없습니다.");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
