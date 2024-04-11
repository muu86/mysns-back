package com.mj.mysns.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserFriend is a Querydsl query type for UserFriend
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserFriend extends EntityPathBase<UserFriend> {

    private static final long serialVersionUID = 2065045527L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserFriend userFriend = new QUserFriend("userFriend");

    public final com.mj.mysns.common.QBaseEntity _super = new com.mj.mysns.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QUser from;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<UserFriend.FriendStatus> status = createEnum("status", UserFriend.FriendStatus.class);

    public final QUser to;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QUserFriend(String variable) {
        this(UserFriend.class, forVariable(variable), INITS);
    }

    public QUserFriend(Path<? extends UserFriend> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserFriend(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserFriend(PathMetadata metadata, PathInits inits) {
        this(UserFriend.class, metadata, inits);
    }

    public QUserFriend(Class<? extends UserFriend> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.from = inits.isInitialized("from") ? new QUser(forProperty("from"), inits.get("from")) : null;
        this.to = inits.isInitialized("to") ? new QUser(forProperty("to"), inits.get("to")) : null;
    }

}

