package com.mj.mysns.chat.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.mj.mysns.chat.entity.ChatMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatMapperTest {

    @Autowired ChatMapper mapper;

    @Test
    void toChatDto() {
    }

    @Test
    void testToChatDto() {
    }

    @Test
    void toChatMessageDto() {
    }

    @Test
    void toChatMessage() {
        ChatMessageDto dto = ChatMessageDto.builder()
            .chatId(1L)
            .sender("sender")
            .message("message")
            .createdAt("오늘")
            .build();

        ChatMessage entity = mapper.toChatMessage(dto);
        assertNull(entity.getId());
        assertNull(entity.getChat());
        assertEquals("sender", entity.getSender());
        assertEquals("message", entity.getMessage());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getModifiedAt());
    }

    @Test
    void testToChatMessageDto() {
    }

    @Test
    void toChatUsername() {
    }

    @Test
    void testToChatUsername() {
    }
}