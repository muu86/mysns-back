package com.mj.mysns.post.repository;

import static com.mj.mysns.location.entity.QAddress.address;
import static com.mj.mysns.post.entity.QComment.comment;
import static com.mj.mysns.post.entity.QPost.post;
import static com.mj.mysns.post.entity.QPostFile.postFile;
import static com.mj.mysns.user.entity.QUser.user;
import static com.mj.mysns.user.entity.QUserAddress.userAddress;
import static com.mj.mysns.user.entity.QUserFile.userFile;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.types.dsl.Expressions.asBoolean;
import static com.querydsl.core.types.dsl.Expressions.predicate;
import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import static org.geolatte.geom.builder.DSL.c;
import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;
import static org.geolatte.geom.crs.CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG;

import com.mj.mysns.common.BaseEntity;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.post.dto.GetComment;
import com.mj.mysns.post.dto.GetPost;
import com.mj.mysns.post.entity.Comment;
import com.mj.mysns.post.entity.Post;
import com.mj.mysns.user.entity.UserAddress;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Ops;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import geotrellis.proj4.CRS;
import geotrellis.proj4.Transform;
import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.geolatte.geom.C2D;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.transaction.annotation.Transactional;
import scala.Tuple2;

@Transactional(readOnly = true)
public class CustomizedPostRepositoryImpl implements CustomizedPostRepository {

    private static final int EPSG_4326 = 4326;
    private static final int EPSG_5179 = 5179;
    private static final double DEFAULT_LAT = 37.59554172008826;
    private static final double DEFAULT_LON = 126.96567699902982;

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public CustomizedPostRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
    }

    @Override
    public List<Post> findPost(GetPost condition) {
        Point<C2D> point = getTargetPoint(condition);

        List<Post> posts = queryFactory.select(post)
            .from(post)
            .leftJoin(post.address, address).fetchJoin()
            .join(post.user, user).fetchJoin()
            .where(
                predicate(Ops.EQ,
                    stringTemplate("st_dwithin ({0}, {1}, {2})", point, address.geo.geoP, condition.distance()),
                    asBoolean(true))
            )
            .orderBy(stringTemplate("st_distance({0}, {1})", point, address.geo.geoP).asc())
            .limit(condition.limit())
            .offset(condition.offset())
            .fetch();

        fetchJoinPostAll(posts);

        return posts;
    }

    @Override
    public List<Post> findUserPost(GetPost condition) {
        List<Post> posts = queryFactory.selectFrom(post)
            .join(post.user, user).fetchJoin()
            .where(user.username.eq(condition.username()))
            .orderBy(post.createdAt.desc())
            .limit(condition.limit())
            .offset(condition.offset())
            .fetch();
        
        fetchJoinPostAll(posts);

        return posts;
    }

    @Override
    public List<Comment> findCommentByPostId(GetComment condition) {
        List<Comment> comments = queryFactory
            .selectFrom(comment)
            .join(comment.post, post).fetchJoin()
            .join(comment.user, user).fetchJoin()
            .leftJoin(user.addresses, userAddress).fetchJoin()
            .where(post.id.eq(condition.postId()))
            .orderBy(comment.createdAt.desc())
            .fetch();

        fetchJoinCommentAll(comments);

        return comments;
    }

    @Override
    public Map<Post, List<Comment>> findCommentByPost(List<Post> posts, int limit) {

        Map<Post, List<Comment>> map = queryFactory
            .from(post)
            .leftJoin(post.comments, comment).fetchJoin()
            .where(post.in(posts))
            .transform(groupBy(post).as(list(comment)));

        map.keySet().forEach(k -> {
            List<Comment> sorted = map.get(k).stream()
                .sorted(Comparator.comparing(BaseEntity::getCreatedAt).reversed())
                .limit(limit).toList();
            map.put(k, sorted);
        });

        List<Comment> comments = map.values().stream().flatMap(Collection::stream).toList();
        fetchJoinCommentAll(comments);

        return map;
    }

    private void fetchJoinPostAll(List<Post> posts) {
        fetchJoinPostFile(posts);

        fetchJoinUserFile(posts);

        fetchJoinUserAddress(posts);
    }

    private void fetchJoinCommentAll(List<Comment> comments) {
        fetchJoinCommentUserAddress(comments);

        fetchJoinCommentUserFile(comments);
    }

    private void fetchJoinCommentUserFile(List<Comment> comments) {
        queryFactory
            .select(comment)
            .from(comment)
            .join(comment.user, user).fetchJoin()
            .leftJoin(user.files, userFile).fetchJoin()
            .where(comment.in(comments))
            .fetch();
    }

    private void fetchJoinCommentUserAddress(List<Comment> comments) {
        queryFactory
            .select(comment)
            .from(comment)
            .join(comment.user, user).fetchJoin()
            .leftJoin(user.addresses, userAddress).fetchJoin()
            .where(comment.in(comments))
            .fetch();
    }

    private void fetchJoinUserAddress(List<Post> posts) {
        queryFactory
            .select(post)
            .from(post)
            .join(post.user, user).fetchJoin()
            .leftJoin(user.addresses, userAddress).fetchJoin()
            .where(post.in(posts))
            .fetch();
    }

    private void fetchJoinUserFile(List<Post> posts) {
        queryFactory
            .select(post)
            .from(post)
            .join(post.user, user).fetchJoin()
            .leftJoin(user.files, userFile).fetchJoin()
            .where(post.in(posts))
            .fetch();
    }

    private void fetchJoinPostFile(List<Post> posts) {
        queryFactory
            .selectFrom(post)
            .leftJoin(post.files, postFile).fetchJoin()
            .where(post.in(posts))
            .fetch();
    }

    private Point<C2D> getTargetPoint(GetPost condition) {
        if (condition.latitude() != null && condition.longitude() != null) {
            return epsg5179(condition.longitude(), condition.latitude());
        }

        if (condition.addressCode() != null && !condition.addressCode().isBlank()) {
            Address found = queryFactory.selectFrom(address)
                .where(address.code.eq(condition.addressCode()))
                .fetchOne();
            if (found != null) return found.getGeo().getCenterP();
        }

        if (condition.targetUsername() != null && !condition.targetUsername().isBlank()) {
            Address targetUserAddress = getTargetUserAddress(condition.targetUsername());
            if (targetUserAddress != null) return targetUserAddress.getGeo().getCenterP();
        }

        return epsg5179(DEFAULT_LON, DEFAULT_LAT);
    }

    private Point<C2D> epsg5179(double longitude, double latitude) {
        Point<G2D> from = point(WGS84, g(longitude, latitude));

        var t = Transform.apply(CRS.fromEpsgCode(EPSG_4326), CRS.fromEpsgCode(EPSG_5179));
        Tuple2<Object, Object> to = t.apply(from.getPosition().getLon(),
            from.getPosition().getLat());
        return point(getProjectedCoordinateReferenceSystemForEPSG(EPSG_5179),
            c((Double) to._1(), (Double) to._2()));
    }

    private Address getTargetUserAddress(String username) {
        List<Tuple> userAddressTuple = queryFactory
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
}
