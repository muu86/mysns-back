package com.mj.mysns.user.repository;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mj.mysns.config.TestConfig;
import com.mj.mysns.user.dto.UserDto;
import com.mj.mysns.user.entity.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.IntStream;
import net.ttddyy.dsproxy.QueryCountHolder;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@ExtendWith(InstancioExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestConfig.class)
class CustomizedUserRepositoryImplTest {

    @Autowired
    UserRepository repository;

    List<User> saved;

    @BeforeEach
    void setup() {
        int size = 10;
        OfInt iter = IntStream.range(0, size).iterator();
        List<User> users = Instancio.ofList(User.class).size(size)
            .ignore(field(User::getId))
            .supply(field(User::getUsername), () -> "test" + iter.next())
            .supply(field(User::getComments), () -> new ArrayList<>())
            .supply(field(User::getAddresses), () -> new HashSet<>())
            .supply(field(User::getFiles), () -> new ArrayList<>())
            .supply(field(User::getRelations), () -> new ArrayList<>())
            .create();

        saved = repository.saveAll(users);

        QueryCountHolder.clear();
    }

    @AfterEach
    void teardown() {
        QueryCountHolder.clear();
    }

    @Test
    void findUserProfile_UsernameExists_ReturnUser() {
        String username = saved.getFirst().getUsername();
        Optional<User> found = repository.findUserProfile(
            UserDto.builder().username(username).build());

        assertTrue(found.isPresent());
        assertEquals(username, found.get().getUsername());

    }

    @Test
    void findUserProfile_NotExistsUsername_ReturnEmpty() {
        Optional<User> found = repository.findUserProfile(
            UserDto.builder().username("아무거나").build());

        assertTrue(found.isEmpty());
    }

    @Test
    void findUserProfile_ListUserDto_ReturnListUser() {
        var dtos = IntStream.range(0, 5).mapToObj(i -> UserDto.builder().username("test" + i).build()).toList();

        List<User> users = repository.findUserProfile(dtos);
        assertEquals(5, users.size());
    }


    @Test
    void findFriends_BeforeAccept_NotFriends() {
        List<User> all = repository.findAll();
        User from = all.get(0);
        User to1 = all.get(1);
        User to2 = all.get(2);

        from.requestFriend(to1);
        from.requestFriend(to2);

        List<User> friends = repository.findFriends(
            UserDto.builder().username(from.getUsername()).build());
        assertEquals(0, friends.size());
    }

    @Test
    void findFriends_AfterAccept_BecomeFriends() {
        List<User> all = repository.findAll();
        User from = all.get(0);
        User to1 = all.get(1);
        User to2 = all.get(2);
        User to3 = all.get(3);
        User to4 = all.get(4);
        User to5 = all.get(5);

        from.requestFriend(to1);
        from.requestFriend(to2);
        from.requestFriend(to3);
        from.requestFriend(to4);
        from.requestFriend(to5);

        to1.acceptFriend(from);
        to5.acceptFriend(from);

        UserDto fromDto = UserDto.builder().username(from.getUsername()).build();
        List<User> foundFriends = repository.findFriends(fromDto);

        assertEquals(2, foundFriends.size());
        assertEquals(5, from.getRelations().size());
        assertTrue(foundFriends.contains(to1));
        assertTrue(foundFriends.contains(to5));
    }

    @Test
    void findFriendsRequestsFromMe_BeforeAccept_RequestStatus() {
        List<User> all = repository.findAll();
        User from = all.get(0);
        User to1 = all.get(1);
        User to2 = all.get(2);
        User to3 = all.get(3);
        User to4 = all.get(4);
        User to5 = all.get(5);

        from.requestFriend(to1);
        from.requestFriend(to2);
        from.requestFriend(to3);
        from.requestFriend(to4);
        from.requestFriend(to5);

        to1.acceptFriend(from);
        to5.acceptFriend(from);

        UserDto fromDto = UserDto.builder().username(from.getUsername()).build();
        List<User> foundRequests = repository.findFriendsRequestsFromMe(fromDto);

        assertEquals(3, foundRequests.size());
        assertEquals(5, from.getRelations().size());
        assertTrue(foundRequests.contains(to2));
        assertTrue(foundRequests.contains(to3));
        assertTrue(foundRequests.contains(to4));
    }


    @WithSettings
    static final Settings settings = Settings.create()
        .set(Keys.JPA_ENABLED, true)
        .set(Keys.BEAN_VALIDATION_ENABLED, true)
        .set(Keys.SET_BACK_REFERENCES, true)
        .lock();

    private long selectQueryCount() {
        return QueryCountHolder.get("DATA_SOURCE_PROXY").getSelect();
    }

}