package com.mj.mysns.chat.repository;

import com.mj.mysns.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, CustomizedChatRepository {

}
