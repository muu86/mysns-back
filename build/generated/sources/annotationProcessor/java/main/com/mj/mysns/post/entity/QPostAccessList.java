package com.mj.mysns.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostAccessList is a Querydsl query type for PostAccessList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostAccessList extends EntityPathBase<PostAccessList> {

    private static final long serialVersionUID = 753780421L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostAccessList postAccessList = new QPostAccessList("postAccessList");

    public final com.mj.mysns.common.QBaseEntity _super = new com.mj.mysns.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QPost post;

    public final com.mj.mysns.user.entity.QUser user;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QPostAccessList(String variable) {
        this(PostAccessList.class, forVariable(variable), INITS);
    }

    public QPostAccessList(Path<? extends PostAccessList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostAccessList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostAccessList(PathMetadata metadata, PathInits inits) {
        this(PostAccessList.class, metadata, inits);
    }

    public QPostAccessList(Class<? extends PostAccessList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new com.mj.mysns.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

