package com.mj.mysns.chat.service;

import com.mj.mysns.chat.dto.ChatDto;
import com.mj.mysns.chat.dto.ChatMapper;
import com.mj.mysns.chat.dto.ChatMessageDto;
import com.mj.mysns.chat.entity.Chat;
import com.mj.mysns.chat.entity.ChatMessage;
import com.mj.mysns.chat.entity.ChatUser;
import com.mj.mysns.chat.exception.ChatNotFoundException;
import com.mj.mysns.chat.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper mapper;

    @Transactional
    public Long createChat(String sender, String receiver) {
        Chat chat = new Chat();
        chat.enterUser(new ChatUser(sender));
        chat.enterUser(new ChatUser(receiver));
        Chat saved = chatRepository.save(chat);
        return saved.getId();
    }

    @Transactional
    public ChatMessageDto saveMessage(ChatMessageDto dto) {
        Chat chat = chatRepository.findById(dto.chatId()).orElseThrow(ChatNotFoundException::new);

        chat.addMessage(ChatMessage.from(dto));
        chatRepository.save(chat);

        return dto;
    }

    public List<ChatDto> getChat(String username) {
        List<Chat> found = chatRepository.findChatByUsername(username);
        List<ChatDto> dtos = mapper.toChatDto(found);
        return dtos;
    }

    public List<ChatMessageDto> getChatMessage(Long chatId, Pageable pageable) {
        return chatRepository.findChatMessage(chatId, pageable)
            .stream().map(mapper::toChatMessageDto).toList();
    }
}
