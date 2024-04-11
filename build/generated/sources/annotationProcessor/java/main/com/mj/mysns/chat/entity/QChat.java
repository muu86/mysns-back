package com.mj.mysns.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChat is a Querydsl query type for Chat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChat extends EntityPathBase<Chat> {

    private static final long serialVersionUID = -1679246413L;

    public static final QChat chat = new QChat("chat");

    public final com.mj.mysns.common.QBaseEntity _super = new com.mj.mysns.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<ChatMessage, QChatMessage> messages = this.<ChatMessage, QChatMessage>createList("messages", ChatMessage.class, QChatMessage.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<Chat.ChatStatus> status = createEnum("status", Chat.ChatStatus.class);

    public final ListPath<ChatUser, QChatUser> users = this.<ChatUser, QChatUser>createList("users", ChatUser.class, QChatUser.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QChat(String variable) {
        super(Chat.class, forVariable(variable));
    }

    public QChat(Path<? extends Chat> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChat(PathMetadata metadata) {
        super(Chat.class, metadata);
    }

}

