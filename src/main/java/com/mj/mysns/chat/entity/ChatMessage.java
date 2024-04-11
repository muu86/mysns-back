package com.mj.mysns.chat.entity;

import com.mj.mysns.chat.dto.ChatMessageDto;
import com.mj.mysns.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ChatMessage extends BaseEntity {

    @ManyToOne
    @Setter(value = AccessLevel.PROTECTED)
    private Chat chat;

    private String sender;

    private String message;

    public static ChatMessage from(ChatMessageDto dto) {
        return ChatMessage.builder()
            .sender(dto.sender())
            .message(dto.message())
            .build();
    }
}
