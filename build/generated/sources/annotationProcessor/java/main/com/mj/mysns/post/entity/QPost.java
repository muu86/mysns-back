package com.mj.mysns.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 1726945859L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.mj.mysns.common.QBaseEntity _super = new com.mj.mysns.common.QBaseEntity(this);

    public final SetPath<PostAccessList, QPostAccessList> accessList = this.<PostAccessList, QPostAccessList>createSet("accessList", PostAccessList.class, QPostAccessList.class, PathInits.DIRECT2);

    public final EnumPath<Post.AccessType> accessType = createEnum("accessType", Post.AccessType.class);

    public final com.mj.mysns.location.entity.QAddress address;

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<PostFile, QPostFile> files = this.<PostFile, QPostFile>createList("files", PostFile.class, QPostFile.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<Post.PostStatus> status = createEnum("status", Post.PostStatus.class);

    public final com.mj.mysns.user.entity.QUser user;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.mj.mysns.location.entity.QAddress(forProperty("address"), inits.get("address")) : null;
        this.user = inits.isInitialized("user") ? new com.mj.mysns.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

