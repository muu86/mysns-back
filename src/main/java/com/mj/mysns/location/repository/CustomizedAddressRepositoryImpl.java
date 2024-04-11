package com.mj.mysns.location.repository;

import static com.mj.mysns.location.entity.QAddress.address;
import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

import com.mj.mysns.location.entity.Address;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

public class CustomizedAddressRepositoryImpl implements CustomizedAddressRepository {

    private final EntityManager em;
    private final JPAQueryFactory qf;

    public CustomizedAddressRepositoryImpl(EntityManager em) {
        this.em = em;
        this.qf = new JPAQueryFactory(em);
    }

    //    select
    //      from   legal_address a
    //    order by st_distancesphere(st_centroid(la1_0.location), ?)
    //    offset   ? rows
    //     fetch  first ? rows only
    @Override
    public List<Address> findLegalAddressNear(double latitude, double longitude, int page, int offset) {

        Point<G2D> point = point(WGS84,
            g(longitude, latitude));

        JPAQuery<Address> query = qf
            .selectFrom(address)
            .orderBy(
                stringTemplate(
                    "st_distancesphere({0}, {1})",
                    stringTemplate("st_centroid({0})", address.geo.geoG), point)
                    .asc())
            .offset(offset)
            .limit(page);

        List<Address> result = query.fetch();
        return result;
    }

}
