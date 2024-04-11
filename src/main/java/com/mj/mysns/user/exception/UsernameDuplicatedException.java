package com.mj.mysns.user.exception;

public class UsernameDuplicatedException extends RuntimeException {

    public UsernameDuplicatedException() {
        super("사용 중인 이름입니다.");
    }

    public UsernameDuplicatedException(String message) {
        super(message);
    }
}
