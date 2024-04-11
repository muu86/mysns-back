package com.mj.mysns.chat.controller;

import com.mj.mysns.chat.entity.Chat.ChatStatus;
import com.mj.mysns.user.dto.UserProfile;
import java.util.List;

public record ChatResult(
    Long id,

    List<UserProfile> users,

    ChatStatus status
) {
}
