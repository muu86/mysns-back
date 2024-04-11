package com.mj.mysns.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAddress is a Querydsl query type for UserAddress
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAddress extends EntityPathBase<UserAddress> {

    private static final long serialVersionUID = -955695717L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAddress userAddress = new QUserAddress("userAddress");

    public final com.mj.mysns.common.QBaseEntity _super = new com.mj.mysns.common.QBaseEntity(this);

    public final com.mj.mysns.location.entity.QAddress address;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<UserAddress.Status> status = createEnum("status", UserAddress.Status.class);

    public final QUser user;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QUserAddress(String variable) {
        this(UserAddress.class, forVariable(variable), INITS);
    }

    public QUserAddress(Path<? extends UserAddress> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAddress(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAddress(PathMetadata metadata, PathInits inits) {
        this(UserAddress.class, metadata, inits);
    }

    public QUserAddress(Class<? extends UserAddress> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.mj.mysns.location.entity.QAddress(forProperty("address"), inits.get("address")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

