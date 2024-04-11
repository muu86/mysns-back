package com.mj.mysns.any;

import static com.mj.mysns.location.entity.QAddress.address;
import static com.mj.mysns.post.entity.QComment.comment;
import static com.mj.mysns.post.entity.QPost.post;
import static com.mj.mysns.user.entity.QUser.user;
import static com.mj.mysns.user.entity.QUserAddress.userAddress;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.types.dsl.Expressions.asBoolean;
import static com.querydsl.core.types.dsl.Expressions.predicate;
import static com.querydsl.core.types.dsl.Expressions.stringPath;
import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import static com.querydsl.spatial.GeometryExpressions.asGeometry;
import static com.querydsl.spatial.SpatialOps.DWITHIN;
import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

import com.mj.mysns.common.BaseEntity;
import com.mj.mysns.common.CustomizedQuerydslTemplate;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.post.entity.Comment;
import com.mj.mysns.post.entity.Post;
import com.mj.mysns.user.entity.UserAddress;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.spatial.GeometryExpression;
import com.querydsl.sql.SQLExpressions;
import jakarta.persistence.EntityManager;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.geolatte.geom.C2D;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CrsRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class QuerydslTest {

    EntityManager em;

    JPAQueryFactory qf;

    @Autowired
    public QuerydslTest(EntityManager em) {
        this.em = em;
        this.qf = new JPAQueryFactory(new CustomizedQuerydslTemplate(), em);
    }

//    @BeforeEach
//    void set() {
//        this.qf = new JPAQueryFactory(em);
//    }

    @Test
    void t1() {
        double latitude = 37.59554172008826;
        double longitude = 126.96567699902982;
        Point<G2D> point = point(WGS84, g(longitude, latitude));
        GeometryExpression<Point<G2D>> pointG = asGeometry(point);
        GeometryExpression<Geometry<G2D>> addressG = asGeometry(
            address.geo.geoG);
        List<Address> addresses1 = qf
            .selectFrom(address)
            .where(addressG.contains(pointG))
            .fetch();
        System.out.println(addresses1);
    }

    @Test
    void t4() {
        double latitude = 37.59554172008826;
        double longitude = 126.96567699902982;
        Point<G2D> point = point(WGS84, g(longitude, latitude));
        List<Address> point1 = em.createQuery("""
                select a
                from Address a
                where contains(a.geo.geoG, :point) = true
                """, Address.class)
            .setParameter("point", point)
            .getResultList();
        Address ad = point1.getFirst();
        System.out.println(ad.getEupmyundong());
    }

    @Test
    void t5() {
        double latitude = 37.59554172008826;
        double longitude = 126.96567699902982;
        Point<G2D> point = point(WGS84, g(longitude, latitude));

        List<Post> list = em.createQuery("""
                SELECT p
                FROM Post p
                LEFT JOIN p.address a
                WHERE st_within(a.geo.geoG, st_buffer(:point, 3)) = true
                """, Post.class)
            .setParameter("point", point)
            .getResultList();

        System.out.println(list);
    }

    @Test
    void t2() {
        qf.select(comment)
            .from(post)
            .join(post.comments, comment)
            .where(post.id.in(List.of(550L)));

        String username = "Aerified";
        Address targetAddress = getTargetAddress(username);

        Expression<String> distanceSub = ExpressionUtils.as(
            stringTemplate(
                "st_distancesphere({0}, {1})",
                stringTemplate("st_centroid({0})", post.address.geo.geoG), targetAddress.getGeo().getGeoG()),
            "distance");

//        List<Tuple> tuple = qf
//            .select(post, distanceSub)
//            .from(post)
//            .join(post.user, user).fetchJoin()
//            .leftJoin(post.legalAddress, legalAddress).fetchJoin()
//            .orderBy(new OrderSpecifier<>(Order.ASC, stringPath("distance")))
//            .limit(10)
//            .offset(0)
//            .fetch();
//        List<Post> posts = tuple.stream().map(t -> t.get(post)).toList();

//        fetchJoinAll(posts);

//        List<PostDto> fetch = qf.select(Projections.fields(PostDto.class, post.id))
//            .from(post)
//            .where(post.id.in(List.of(550L)))
//            .fetch();


        List<Tuple> fetch = qf.select(post, distanceSub)
            .from(post)
            .join(post.user, user).fetchJoin()
            .leftJoin(post.address, address).fetchJoin()
            .orderBy(new OrderSpecifier<>(Order.ASC, stringPath("distance")))
            .limit(10)
            .offset(0)
            .fetch();
        List<Post> list = fetch.stream().map(t -> t.get(post)).toList();

        Map<Post, List<Comment>> map = qf.from(post, comment)
            .where(comment.post.id.eq(post.id)
                .and(post.in(list)))
            .transform(groupBy(post).as(list(comment)));

        map.keySet().forEach(k -> {
            List<Comment> comments = map.get(k);
            List<Comment> sorted = comments.stream()
                .sorted(Comparator.comparing(BaseEntity::getCreatedAt).reversed()).limit(3).toList();
//            List<Comment> limited = sorted.stream().limit(3).toList();
//            map.put(k, limited);
            map.put(k, sorted);
        });

//        List<Post> fetch1 = qf.selectFrom(post)
//            .leftJoin(post.comments, comment)
//            .where(post.id.in(map.keySet())
//                .and(comment.in(map.get(post.id))))
//            .fetch();

        System.out.println();
    }

    @Test
    void t3() {
        qf.selectFrom(comment)
            .join(comment.user, user).fetchJoin()
            .leftJoin(user.addresses, userAddress).fetchJoin()
            .where(comment.id.eq(1L))
            .fetch();

        System.out.println();
    }

    private Address getTargetAddress(String username) {
        List<Tuple> userAddressTuple = qf
            .select(address, userAddress.status)
            .from(userAddress)
            .join(userAddress.address, address)
            .join(userAddress.user, user)
            .where(userAddress.status.ne(com.mj.mysns.user.entity.UserAddress.Status.INACTIVE)
                .and(user.username.eq(username)))
            .fetch();

        Optional<Tuple> main = userAddressTuple.stream()
            .filter(t -> UserAddress.Status.MAIN.equals(t.get(userAddress.status))).findFirst();
        Optional<Tuple> active = userAddressTuple.stream()
            .filter(t -> UserAddress.Status.ACTIVE.equals((t.get(userAddress.status)))).findFirst();

        if (main.isPresent()) {
            return main.get().get(address);
        }
        else if (active.isPresent()) {
            return active.get().get(address);
        }
        return null;
    }

    @Test
    void t6() {
        double latitude = 37.59554172008826;
        double longitude = 126.96567699902982;
//        Point<G2D> point = point(WGS84, g(longitude, latitude));
        String username = "test";
        double distance = 1000;

        Point<C2D> point1 = point(CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179),
            DSL.c(952606, 1956133));
//        BooleanOperation predicate = predicate(WITHIN,
//            asGeometry(point), address.geo.geoG);

//        List<Address> fetch = qf
//            .select(address)
//            .from(address)
//            .where(asGeometry(address.geo.geoG).within(asGeometry(point).buffer(1000)))
//            .where(Expressions.stringTemplate("st_dwithin({0}, {1}, {2})", address.geo.geoG, point, 1000.0).eq(Expressions.stringTemplate("true")))
//            .fetch();
//        List resultList = em.createNativeQuery("""
//                SELECT p.*
//                FROM   posts p
//                LEFT JOIN address a on p.address_id = a.id
//                WHERE  st_dwithin(:point, a.centerp, :distance) = true
//                """, Post.class)
//            .setParameter("point", point1)
//            .setParameter("distance", 200)
//            .getResultList();
//
//        List<String> fetch = qf.select(
//                stringTemplate("st_dwithin ({0}, {1}, {2})", point1, address.geo.geoP, 1000.0))
//            .from(address)
//            .fetch();

        List<Post> posts = qf.select(post)
            .from(post)
            .leftJoin(post.address, address).fetchJoin()
            .join(post.user, user).fetchJoin()
            .where(
                predicate(Ops.EQ,
                    stringTemplate("st_dwithin ({0}, {1}, {2})", point1, address.geo.geoP, 1000.0),
                    asBoolean(true))
            )
            .orderBy(stringTemplate("st_distance({0}, {1})", point1, address.geo.geoP).asc())
            .limit(10)
            .offset(0)
            .fetch();

        qf.select(post)
            .from(post)
            .leftJoin(post.address, address).fetchJoin()
            .join(post.user, user).fetchJoin()
            .where(
                predicate(Ops.EQ,
                    stringTemplate("st_dwithin ({0}, {1}, {2})", point1, address.geo.geoP, 1000.0),
                    asBoolean(true))
            )
            .orderBy(stringTemplate("st_distance({0}, {1})", point1, address.geo.geoP).asc())
            .limit(10)
            .offset(0)
                                   .fetch();
//
//        QComment subComment = new QComment("subComment");
//        qf.select(comment)
//            .from(comment)
//            .join(JPAExpressions.select(
//                stringTemplate("row_number() over(partition by {0} order by {1} desc)", post.id, subComment.createdAt).as("row_number"))
//            .from(subComment)
//            .join(subComment.post, post))

//        Map<String, List<Comment>> transform = qf.select(comment,
//                stringTemplate("row_number() over(partition by {0} order by {1} desc)", post.id,
//                    comment.createdAt).as("row_number"))
//            .from(comment)
//            .join(comment.post, post)
//            .transform(groupBy(stringPath("row_number")).as(list(comment)));
//
        List<String> fetch = qf.select(
                stringTemplate("row_number() over(partition by {0} order by {1} desc)", post.id,
                    comment.createdAt).as("row_number"))
            .from(comment)
            .join(comment.post, post)
            .fetch();

        List<Long> fetch1 = qf.select(
                SQLExpressions.rowNumber().over().partitionBy(post).orderBy(comment.id.desc()))
            .from(comment)
            .join(comment.post, post)
            .fetch();

        System.out.println();
    }

    @Test
    void t7() {
//        List<Long> fetch = qf.select(
//                SQLExpressions.rowNumber().over().partitionBy(post).orderBy(comment.createdAt))
//            .from(comment)
//            .join(comment.post, post)
//            .fetch();

        Address address1 = qf.selectFrom(address)
            .where(address.code.eq("1"))
            .fetchOne();
        System.out.println();

    }

    private BooleanExpression dwithin(EntityPath<Address> address) {

//        return predicate(DWITHIN, address)
        return null;
    }

//    private BooleanExpression dwithin(double longitude, double latitude, Integer distance) {
//        Point<G2D> point = point(WGS84, g(longitude, latitude));
//        return predicate(DWITHIN, asGeometry(point), Expressions.path(Geometry<G2D>.class, ), Expressions.constant(distance));
//    }

    private BooleanExpression dwithinWithDefault(Integer distance) {
        double latitude = 37.59554172008826;
        double longitude = 126.96567699902982;
        Point<G2D> point = point(WGS84, g(longitude, latitude));
        return predicate(DWITHIN, asGeometry(point), address.geo.geoG, Expressions.asNumber(distance));
    }

}
