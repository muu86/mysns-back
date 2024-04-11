package com.mj.mysns.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("user 를 찾을 수 없습니다");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
