package com.mj.mysns.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QClaims is a Querydsl query type for Claims
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QClaims extends BeanPath<Claims> {

    private static final long serialVersionUID = 1627385989L;

    public static final QClaims claims = new QClaims("claims");

    public final StringPath email = createString("email");

    public final BooleanPath emailVerified = createBoolean("emailVerified");

    public final StringPath first = createString("first");

    public final StringPath issuer = createString("issuer");

    public final StringPath last = createString("last");

    public final StringPath subject = createString("subject");

    public QClaims(String variable) {
        super(Claims.class, forVariable(variable));
    }

    public QClaims(Path<? extends Claims> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClaims(PathMetadata metadata) {
        super(Claims.class, metadata);
    }

}

