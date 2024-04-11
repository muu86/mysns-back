package com.mj.mysns.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -15640551L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.mj.mysns.common.QBaseEntity _super = new com.mj.mysns.common.QBaseEntity(this);

    public final SetPath<UserAddress, QUserAddress> addresses = this.<UserAddress, QUserAddress>createSet("addresses", UserAddress.class, QUserAddress.class, PathInits.DIRECT2);

    public final NumberPath<Integer> babyMonths = createNumber("babyMonths", Integer.class);

    public final QClaims claims;

    public final ListPath<com.mj.mysns.post.entity.Comment, com.mj.mysns.post.entity.QComment> comments = this.<com.mj.mysns.post.entity.Comment, com.mj.mysns.post.entity.QComment>createList("comments", com.mj.mysns.post.entity.Comment.class, com.mj.mysns.post.entity.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<UserFile, QUserFile> files = this.<UserFile, QUserFile>createList("files", UserFile.class, QUserFile.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<UserFriend, QUserFriend> relations = this.<UserFriend, QUserFriend>createList("relations", UserFriend.class, QUserFriend.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.claims = inits.isInitialized("claims") ? new QClaims(forProperty("claims")) : null;
    }

}

