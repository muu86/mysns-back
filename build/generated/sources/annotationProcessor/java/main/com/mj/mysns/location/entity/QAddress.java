package com.mj.mysns.location.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAddress extends EntityPathBase<Address> {

    private static final long serialVersionUID = 1769046716L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAddress address = new QAddress("address");

    public final com.mj.mysns.common.QBaseEntity _super = new com.mj.mysns.common.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath eupmyundong = createString("eupmyundong");

    public final QGeodata geo;

    public final StringPath gungu = createString("gungu");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath li = createString("li");

    public final QAddressMetadata metadata;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath sido = createString("sido");

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QAddress(String variable) {
        this(Address.class, forVariable(variable), INITS);
    }

    public QAddress(Path<? extends Address> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAddress(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAddress(PathMetadata metadata, PathInits inits) {
        this(Address.class, metadata, inits);
    }

    public QAddress(Class<? extends Address> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.geo = inits.isInitialized("geo") ? new QGeodata(forProperty("geo")) : null;
        this.metadata = inits.isInitialized("metadata") ? new QAddressMetadata(forProperty("metadata")) : null;
    }

}

