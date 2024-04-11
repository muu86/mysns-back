package com.mj.mysns.user.repository;

import static com.mj.mysns.user.entity.QUser.user;
import static com.mj.mysns.user.entity.QUserAddress.userAddress;
import static com.mj.mysns.user.entity.QUserFile.userFile;
import static com.mj.mysns.user.entity.QUserFriend.userFriend;

import com.mj.mysns.user.dto.UserDto;
import com.mj.mysns.user.entity.QUser;
import com.mj.mysns.user.entity.QUserFriend;
import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.entity.UserFriend.FriendStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class CustomizedUserRepositoryImpl implements
    CustomizedUserRepository {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public CustomizedUserRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<User> findUserProfile(UserDto userDto) {
        User found = queryFactory
            .selectFrom(user).distinct()
            .leftJoin(user.addresses, userAddress).fetchJoin()
            .where(user.username.eq(userDto.username()))
            .fetchOne();

        if (found == null) return Optional.empty();

        found = queryFactory
            .selectFrom(user).distinct()
            .leftJoin(user.files, userFile).fetchJoin()
            .where(user.eq(found))
            .fetchOne();

        return Optional.ofNullable(found);
    }

    @Override
    public List<User> findUserProfile(List<UserDto> userDto) {
        List<String> usernames = userDto.stream().map(UserDto::username).toList();

        List<User> found = queryFactory
            .selectFrom(user)
            .leftJoin(user.addresses, userAddress).fetchJoin()
            .where(user.username.in(usernames))
            .fetch();

        if (found == null) return new ArrayList<>();

        found = queryFactory
            .selectFrom(user)
            .leftJoin(user.files, userFile).fetchJoin()
            .where(user.in(found))
            .fetch();

        return found;
    }

    @Override
    public List<User> findFriends(UserDto userDto) {
        if (userDto.username() == null) return Collections.emptyList();

        QUserFriend uf1 = new QUserFriend("uf1");
        QUserFriend uf2 = new QUserFriend("uf2");
        QUser u1 = new QUser("u1");
        QUser u2 = new QUser("u2");

        return queryFactory
            .select(u1)
            .from(uf1)
            .join(u1).on(u1.eq(uf1.to))
            .where(uf1.status.eq(FriendStatus.ACCEPT)
                .and(u1.in(
                    JPAExpressions
                        .select(uf2.from)
                        .from(uf2)
                        .join(u2).on(u2.eq(uf2.to))
                        .where(uf2.status.eq(FriendStatus.ACCEPT)
                            .and(u2.username.eq(userDto.username()))))))
            .fetch();
    }

    @Override
    public List<User> findFriendsRequestsFromMe(UserDto userDto) {
        if (userDto.username() == null) return Collections.emptyList();

        return queryFactory
            .select(userFriend.to)
            .from(userFriend)
            .join(user).on(user.eq(userFriend.from))
            .where(user.username.eq(userDto.username())
                .and(userFriend.status.eq(FriendStatus.REQUEST)))
            .fetch();

    }

    @Override
    public List<User> findFriendsRequestsToMe(UserDto userDto) {
        if (userDto.username() == null) return Collections.emptyList();

        return queryFactory
            .select(userFriend.from)
            .from(userFriend)
            .join(user).on(user.eq(userFriend.to))
            .where(user.username.eq(userDto.username())
                .and(userFriend.status.eq(FriendStatus.REQUEST)))
            .fetch();
    }

}
