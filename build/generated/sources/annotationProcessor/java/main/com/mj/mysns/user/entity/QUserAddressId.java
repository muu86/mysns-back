package com.mj.mysns.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserAddressId is a Querydsl query type for UserAddressId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserAddressId extends BeanPath<UserAddressId> {

    private static final long serialVersionUID = 699419670L;

    public static final QUserAddressId userAddressId = new QUserAddressId("userAddressId");

    public final NumberPath<Long> legalAddressId = createNumber("legalAddressId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserAddressId(String variable) {
        super(UserAddressId.class, forVariable(variable));
    }

    public QUserAddressId(Path<? extends UserAddressId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAddressId(PathMetadata metadata) {
        super(UserAddressId.class, metadata);
    }

}

