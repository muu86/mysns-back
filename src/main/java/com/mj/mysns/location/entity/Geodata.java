package com.mj.mysns.location.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.geolatte.geom.C2D;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.Point;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonIgnoreType
public class Geodata {

    // mysql
    //@Column(nullable = false, columnDefinition = "GEOMETRY SRID 4326")
    @Column(nullable = false, columnDefinition = "geometry(Geometry, 5179)")
    private Geometry<C2D> geoP;

    @Column(nullable = false, columnDefinition = "geometry(Point, 5179)")
    private Point<C2D> centerP;



    @Column(nullable = false, columnDefinition = "geometry(Geometry, 4326)")
    private Geometry<G2D> geoG;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point<G2D> centerG;
}
