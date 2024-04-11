package com.mj.mysns.chat.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mj.mysns.chat.entity.Chat;
import com.mj.mysns.chat.entity.ChatMessage;
import com.mj.mysns.chat.entity.ChatUser;
import com.mj.mysns.config.TestConfig;
import java.util.List;
import java.util.Optional;
import net.ttddyy.dsproxy.QueryCountHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestConfig.class)
class CustomizedChatRepositoryImplTest {

    private static final String DATASOURCE_NAME = "DATA_SOURCE_PROXY";

    @Autowired ChatRepository repository;

    Chat chat;
    @BeforeEach
    void setup() {
        List<ChatUser> chatUsers = List.of(new ChatUser("t1"), new ChatUser("t2"));
        chat = new Chat();
        chatUsers.forEach(chat::enterUser);

        chat = repository.save(chat);

        QueryCountHolder.clear();
    }

    @AfterEach
    void teardown() {
        QueryCountHolder.clear();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void findChat() {
        Optional<Chat> byId = repository.findChat(chat.getId());
        assertTrue(byId.isPresent());
        assertNotSame(chat, byId.get());
        Chat chat = byId.get();

        chat.getUsers().getFirst().getUsername();

        assertEquals(1, QueryCountHolder.get(DATASOURCE_NAME).getSelect());
    }

    @Test
    void findChatByUsername() {
        List<Chat> chatList = repository.findChatByUsername("t1");

        assertEquals(1, chatList.size());

        chat.getUsers().getFirst().getUsername();
        assertEquals(1, QueryCountHolder.get(DATASOURCE_NAME).getSelect());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void findChatMessage() {
        Chat chat = new Chat();
        ChatMessage m1 = ChatMessage.builder().sender("t1").message("hi").build();
        ChatMessage m2 = ChatMessage.builder().sender("t2").message("hihi").build();
        chat.addMessage(m1);
        chat.addMessage(m2);
        repository.save(chat);

        var pageRequest = PageRequest.of(0, 1);
        var messages = repository.findChatMessage(chat.getId(), pageRequest);

        assertEquals(1, messages.size());
        assertEquals("t2", messages.getFirst().getSender());

        var pageRequest2 = PageRequest.of(1, 1);
        var messages2 = repository.findChatMessage(chat.getId(), pageRequest2);
        assertEquals(1, messages2.size());
        assertEquals("t1", messages2.getFirst().getSender());
    }

}