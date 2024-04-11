package com.mj.mysns.chat.repository;

import com.mj.mysns.chat.entity.Chat;
import com.mj.mysns.chat.entity.ChatMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface CustomizedChatRepository {

    Optional<Chat> findChat(Long id);

    List<Chat> findChatByUsername(String username);

    List<ChatMessage> findChatMessage(Long chatId, Pageable pageable);
}
