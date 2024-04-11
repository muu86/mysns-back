package com.mj.mysns.chat.exception;

public class ChatNotFoundException extends RuntimeException {

    public ChatNotFoundException() {
        super("메시지를 찾을 수 없습니다.");
    }

    public ChatNotFoundException(String message) {
        super(message);
    }
}
