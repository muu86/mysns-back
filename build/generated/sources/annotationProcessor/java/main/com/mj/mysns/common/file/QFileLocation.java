package com.mj.mysns.common.file;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFileLocation is a Querydsl query type for FileLocation
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QFileLocation extends BeanPath<FileLocation> {

    private static final long serialVersionUID = -743132154L;

    public static final QFileLocation fileLocation = new QFileLocation("fileLocation");

    public final StringPath key = createString("key");

    public final EnumPath<FileLocation.FileLocationType> type = createEnum("type", FileLocation.FileLocationType.class);

    public QFileLocation(String variable) {
        super(FileLocation.class, forVariable(variable));
    }

    public QFileLocation(Path<? extends FileLocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileLocation(PathMetadata metadata) {
        super(FileLocation.class, metadata);
    }

}

