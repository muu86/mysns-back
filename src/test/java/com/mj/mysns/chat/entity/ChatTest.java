package com.mj.mysns.chat.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatTest {

    @Mock ChatUser user;
    @Mock ChatMessage message;

    @Test
    void enterUser() {
        Chat chat = new Chat();
        chat.enterUser(user);
        assertNotNull(chat.getUsers());
        assertEquals(1, chat.getUsers().size());
    }

    @Test
    void enterUser_ExistingUser_NotAddUser() {
        Chat chat = new Chat();
        chat.enterUser(user);
        chat.enterUser(user);

        assertNotNull(chat.getUsers());
        assertEquals(1, chat.getUsers().size());
    }

    @Test
    void exitUser() {
        Chat chat = new Chat();
        chat.enterUser(user);
        assertNotNull(chat.getUsers());
        assertEquals(1, chat.getUsers().size());

        chat.exitUser(user);
        assertEquals(0, chat.getUsers().size());
    }

    @Test
    void addMessage() {
        Chat chat = new Chat();
        ChatMessage m1 = ChatMessage.builder().sender("t1").message("hi").build();
        ChatMessage m2 = ChatMessage.builder().sender("t2").message("hihi").build();
        chat.addMessage(m1);
        chat.addMessage(m2);

        assertNotNull(chat.getMessages());
        assertEquals(2, chat.getMessages().size());
    }
}