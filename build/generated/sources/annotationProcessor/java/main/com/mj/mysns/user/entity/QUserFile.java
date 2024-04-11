package com.mj.mysns.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserFile is a Querydsl query type for UserFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserFile extends EntityPathBase<UserFile> {

    private static final long serialVersionUID = -400093899L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserFile userFile = new QUserFile("userFile");

    public final com.mj.mysns.common.QBaseEntity _super = new com.mj.mysns.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.mj.mysns.common.file.QFile file;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QUser user;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QUserFile(String variable) {
        this(UserFile.class, forVariable(variable), INITS);
    }

    public QUserFile(Path<? extends UserFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserFile(PathMetadata metadata, PathInits inits) {
        this(UserFile.class, metadata, inits);
    }

    public QUserFile(Class<? extends UserFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.file = inits.isInitialized("file") ? new com.mj.mysns.common.file.QFile(forProperty("file"), inits.get("file")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

