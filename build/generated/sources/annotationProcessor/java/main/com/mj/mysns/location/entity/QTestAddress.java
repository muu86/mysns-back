package com.mj.mysns.location.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTestAddress is a Querydsl query type for TestAddress
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTestAddress extends EntityPathBase<TestAddress> {

    private static final long serialVersionUID = 1906985418L;

    public static final QTestAddress testAddress = new QTestAddress("testAddress");

    public final com.mj.mysns.common.QBaseEntity _super = new com.mj.mysns.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ComparablePath<org.locationtech.jts.geom.Geometry> geo = createComparable("geo", org.locationtech.jts.geom.Geometry.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QTestAddress(String variable) {
        super(TestAddress.class, forVariable(variable));
    }

    public QTestAddress(Path<? extends TestAddress> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTestAddress(PathMetadata metadata) {
        super(TestAddress.class, metadata);
    }

}

