package com.mj.mysns.user.exception;

public class UserDuplicatedException extends RuntimeException {

    public UserDuplicatedException() {
        super("가입한 user 입니다.");
    }

    public UserDuplicatedException(String message) {
        super(message);
    }
}
