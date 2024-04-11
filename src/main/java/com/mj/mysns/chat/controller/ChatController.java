package com.mj.mysns.chat.controller;

import com.mj.mysns.chat.dto.ChatDto;
import com.mj.mysns.chat.dto.ChatMessageDto;
import com.mj.mysns.chat.service.ChatService;
import com.mj.mysns.user.dto.UserDto;
import com.mj.mysns.user.dto.UserDto.UserDtoBuilder;
import com.mj.mysns.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void chatMessage(@Payload ChatMessageDto messageDto) {
        ChatMessageDto saved = chatService.saveMessage(messageDto);
//        messagingTemplate.convertAndSendToUser(saved.receiver(),
//            "/queue/message", saved);
        messagingTemplate.convertAndSend("/chat/%s".formatted(saved.chatId()), saved);
    }

    @PostMapping(path = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Long> createChat(@RequestBody CreateChat createChat) {
        Long chatId = chatService.createChat(createChat.sender(), createChat.receiver());
        return ResponseEntity.ok(chatId);
    }

    @GetMapping("/chat")
    @ResponseBody
    public ResponseEntity<List<ChatResult>> getChat(
        @RequestParam String username) {
        if (username.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        List<ChatDto> chat = chatService.getChat(username);
        List<ChatResult> result = chat.stream().map(c -> {
            var userDtos = c.usernames().stream()
                .map(UserDto.builder()::username)
                .map(UserDtoBuilder::build).toList();
            var userProfiles = userService.getUserProfileByUsername(userDtos);

            return new ChatResult(c.id(), userProfiles, c.status());
        }).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/chat/message")
    @ResponseBody
    public ResponseEntity<List<ChatMessageDto>> getChatMessage(
        @RequestParam Long chatId,
        Pageable pageable) {

        return ResponseEntity.ok(chatService.getChatMessage(chatId, pageable));
    }
}
