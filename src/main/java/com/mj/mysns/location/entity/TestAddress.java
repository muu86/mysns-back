package com.mj.mysns.location.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mj.mysns.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

@Entity
@NoArgsConstructor
@Getter
@Table(indexes = @Index(columnList = "geo"))
public class TestAddress extends BaseEntity {

    @Column(nullable = false, columnDefinition = "geometry(Geometry, 4326)")
    @JsonIgnore
//    private Geometry<G2D> geo;
    private Geometry geo;
}
