package com.mj.mysns.chat.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatUserTest {

    @Mock Chat chat;

    @Test
    void enter() {
        ChatUser user = new ChatUser("test");
        when(chat.getId()).thenReturn(1L);

        user.enter(chat);

        assertEquals(1L, user.getChat().getId());
    }

    @Test
    void exit() {
        ChatUser user = new ChatUser("test");

        user.enter(chat);
        user.exit(chat);

        assertNull(user.getChat());
    }
}