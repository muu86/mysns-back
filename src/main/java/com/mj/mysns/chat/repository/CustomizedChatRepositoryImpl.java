package com.mj.mysns.chat.repository;

import static com.mj.mysns.chat.entity.QChat.chat;
import static com.mj.mysns.chat.entity.QChatMessage.chatMessage;
import static com.mj.mysns.chat.entity.QChatUser.chatUser;

import com.mj.mysns.chat.entity.Chat;
import com.mj.mysns.chat.entity.ChatMessage;
import com.mj.mysns.chat.entity.QChatUser;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class CustomizedChatRepositoryImpl implements CustomizedChatRepository {

    private final JPAQueryFactory qf;

    public CustomizedChatRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Chat> findChat(Long id) {
        Chat found = qf.selectFrom(chat)
            .join(chat.users, chatUser).fetchJoin()
            .where(chat.id.eq(id))
            .fetchOne();
        return Optional.ofNullable(found);
    }

    @Override
    public List<Chat> findChatByUsername(String username) {
        QChatUser sub = new QChatUser("sub");
        List<Chat> fetch1 = qf.select(chat)
            .from(chat)
            .join(chat.users, chatUser).fetchJoin()
            .where(chat.in(JPAExpressions.select(sub.chat).from(sub).where(sub.username.eq(username))))
            .fetch();
        return fetch1;
    }

    @Override
    public List<ChatMessage> findChatMessage(Long chatId, Pageable pageable) {
        return qf.select(chatMessage)
            .from(chatMessage)
            .join(chatMessage.chat, chat)
            .where(chat.id.eq(chatId))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(chatMessage.createdAt.desc())
            .fetch();
    }
}
