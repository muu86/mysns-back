package com.mj.mysns.chat.dto;

import com.mj.mysns.chat.entity.Chat;
import com.mj.mysns.chat.entity.ChatMessage;
import com.mj.mysns.common.DateMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-12T03:29:59+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class ChatMapperImpl extends ChatMapper {

    @Autowired
    private DateMapper dateMapper;

    @Override
    public ChatDto toChatDto(Chat chat) {
        if ( chat == null ) {
            return null;
        }

        List<String> usernames = null;
        Long id = null;
        Chat.ChatStatus status = null;

        usernames = toChatUsername( chat.getUsers() );
        id = chat.getId();
        status = chat.getStatus();

        ChatDto chatDto = new ChatDto( id, usernames, status );

        return chatDto;
    }

    @Override
    public List<ChatDto> toChatDto(List<Chat> chat) {
        if ( chat == null ) {
            return null;
        }

        List<ChatDto> list = new ArrayList<ChatDto>( chat.size() );
        for ( Chat chat1 : chat ) {
            list.add( toChatDto( chat1 ) );
        }

        return list;
    }

    @Override
    public ChatMessageDto toChatMessageDto(ChatMessage chatMessage) {
        if ( chatMessage == null ) {
            return null;
        }

        ChatMessageDto.ChatMessageDtoBuilder chatMessageDto = ChatMessageDto.builder();

        chatMessageDto.chatId( chatMessageChatId( chatMessage ) );
        chatMessageDto.sender( chatMessage.getSender() );
        chatMessageDto.message( chatMessage.getMessage() );
        chatMessageDto.createdAt( dateMapper.toString( chatMessage.getCreatedAt() ) );

        return chatMessageDto.build();
    }

    @Override
    public ChatMessage toChatMessage(ChatMessageDto chatMessageDto) {
        if ( chatMessageDto == null ) {
            return null;
        }

        ChatMessage.ChatMessageBuilder chatMessage = ChatMessage.builder();

        chatMessage.sender( chatMessageDto.sender() );
        chatMessage.message( chatMessageDto.message() );

        return chatMessage.build();
    }

    @Override
    public List<ChatMessageDto> toChatMessageDto(List<ChatMessage> chatMessages) {
        if ( chatMessages == null ) {
            return null;
        }

        List<ChatMessageDto> list = new ArrayList<ChatMessageDto>( chatMessages.size() );
        for ( ChatMessage chatMessage : chatMessages ) {
            list.add( toChatMessageDto( chatMessage ) );
        }

        return list;
    }

    private Long chatMessageChatId(ChatMessage chatMessage) {
        if ( chatMessage == null ) {
            return null;
        }
        Chat chat = chatMessage.getChat();
        if ( chat == null ) {
            return null;
        }
        Long id = chat.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
