package com.mj.mysns.location.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGeodata is a Querydsl query type for Geodata
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QGeodata extends BeanPath<Geodata> {

    private static final long serialVersionUID = -1457498813L;

    public static final QGeodata geodata = new QGeodata("geodata");

    public final SimplePath<org.geolatte.geom.Point<org.geolatte.geom.G2D>> centerG = createSimple("centerG", org.geolatte.geom.Point.class);

    public final SimplePath<org.geolatte.geom.Point<org.geolatte.geom.C2D>> centerP = createSimple("centerP", org.geolatte.geom.Point.class);

    public final SimplePath<org.geolatte.geom.Geometry<org.geolatte.geom.G2D>> geoG = createSimple("geoG", org.geolatte.geom.Geometry.class);

    public final SimplePath<org.geolatte.geom.Geometry<org.geolatte.geom.C2D>> geoP = createSimple("geoP", org.geolatte.geom.Geometry.class);

    public QGeodata(String variable) {
        super(Geodata.class, forVariable(variable));
    }

    public QGeodata(Path<? extends Geodata> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGeodata(PathMetadata metadata) {
        super(Geodata.class, metadata);
    }

}

