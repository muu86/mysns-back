package com.mj.mysns.chat.dto;


import com.mj.mysns.chat.entity.Chat.ChatStatus;
import java.util.List;

public record ChatDto(
    Long id,

    List<String> usernames,

    ChatStatus status
) {}
