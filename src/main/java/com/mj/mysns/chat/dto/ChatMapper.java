package com.mj.mysns.chat.dto;

import com.mj.mysns.chat.entity.Chat;
import com.mj.mysns.chat.entity.ChatMessage;
import com.mj.mysns.chat.entity.ChatUser;
import com.mj.mysns.common.DateMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = { DateMapper.class })
public abstract class ChatMapper {

    @Mapping(target = "usernames", source = "users")
    public abstract ChatDto toChatDto(Chat chat);

    public abstract List<ChatDto> toChatDto(List<Chat> chat);

    @Mapping(target = "chatId", source = "chat.id")
    public abstract ChatMessageDto toChatMessageDto(ChatMessage chatMessage);

    @Mapping(ignore = true, target = "chat")
    public abstract ChatMessage toChatMessage(ChatMessageDto chatMessageDto);

    public abstract List<ChatMessageDto> toChatMessageDto(List<ChatMessage> chatMessages);

    public String toChatUsername(ChatUser chatUser) {
        return chatUser != null ? chatUser.getUsername() : null;
    }

    public List<String> toChatUsername(List<ChatUser> chatUsers) {
        if (chatUsers == null) return null;
        return chatUsers.stream().map(ChatUser::getUsername).toList();
    }
}
