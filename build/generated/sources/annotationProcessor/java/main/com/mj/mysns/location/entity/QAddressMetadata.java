package com.mj.mysns.location.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAddressMetadata is a Querydsl query type for AddressMetadata
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAddressMetadata extends BeanPath<AddressMetadata> {

    private static final long serialVersionUID = -199694837L;

    public static final QAddressMetadata addressMetadata = new QAddressMetadata("addressMetadata");

    public final DateTimePath<java.time.LocalDateTime> metaCreatedAt = createDateTime("metaCreatedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> metaDeletedAt = createDateTime("metaDeletedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> metaModifiedAt = createDateTime("metaModifiedAt", java.time.LocalDateTime.class);

    public final StringPath prevCode = createString("prevCode");

    public final StringPath sunwi = createString("sunwi");

    public QAddressMetadata(String variable) {
        super(AddressMetadata.class, forVariable(variable));
    }

    public QAddressMetadata(Path<? extends AddressMetadata> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAddressMetadata(PathMetadata metadata) {
        super(AddressMetadata.class, metadata);
    }

}

