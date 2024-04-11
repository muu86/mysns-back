package com.mj.mysns.chat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mj.mysns.chat.dto.ChatMessageDto;
import com.mj.mysns.chat.entity.Chat;
import com.mj.mysns.chat.exception.ChatNotFoundException;
import com.mj.mysns.chat.repository.ChatRepository;
import com.mj.mysns.chat.service.ChatService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class ChatServiceTest {

    @Autowired
    ChatService service;
    @MockBean ChatRepository repository;

    @Test
    void save_NotFoundChatId_ThrowsChatNotFoundException() {
        var dto = ChatMessageDto.builder().chatId(1L).message("hi").sender("sender")
            .build();
        when(repository.findChat(anyLong())).thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> {
            service.saveMessage(dto);
        });
    }

    @Test
    void save_Success() {
        var dto = ChatMessageDto.builder().chatId(1L).message("hi").sender("sender")
            .build();
        var chat = mock(Chat.class);
        when(repository.findById(anyLong())).thenReturn(Optional.of(chat));

        service.saveMessage(dto);

        verify(repository).save(chat);
    }

    @Test
    void getChat() {
        var username = "test";
        when(repository.findChatByUsername(anyString())).thenReturn(new ArrayList<>());

        var chat = service.getChat(username);
        assertEquals(0, chat.size());

        verify(repository).findChatByUsername(eq(username));
    }

    @Test
    void getChatMessage() {
        var pageRequest = PageRequest.of(0, 10);

        service.getChatMessage(1L, pageRequest);

        verify(repository).findChatMessage(1L, pageRequest);
    }
}