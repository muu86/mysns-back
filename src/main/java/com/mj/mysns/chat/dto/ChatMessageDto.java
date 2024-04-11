package com.mj.mysns.chat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChatMessageDto(

    @NotNull
    Long chatId,

    String sender,

    String message,

    String createdAt
) {}
