package com.mj.mysns.post.repository;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mj.mysns.common.file.File;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.location.repository.AddressRepository;
import com.mj.mysns.post.dto.GetComment;
import com.mj.mysns.post.dto.GetPost;
import com.mj.mysns.post.entity.Comment;
import com.mj.mysns.post.entity.Post;
import com.mj.mysns.post.entity.PostFile;
import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(properties = { "init.db.address = true" })
@ExtendWith(InstancioExtension.class)
@Transactional
@Testcontainers
public class CustomizedPostRepositoryImplTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        DockerImageName.parse("postgis/postgis:16-master").asCompatibleSubstituteFor("postgres"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired PostRepository postRepository;

    @Autowired UserRepository userRepository;

    @Autowired AddressRepository addressRepository;

    @Autowired
    EntityManager em;

    @Test
    void findPostNear_3Post_EachHas_3PostFile_Return3PostAndEachHas3PostFile() {

        double latitude = 37.59554172008826;
        double longitude = 126.96567699902982;
        int offset = 0;

        List<String> addresses = new ArrayList<>();
        addresses.add("서울특별시 종로구 세종로");
        addresses.add("서울특별시 중구 삼각동");
        addresses.add("서울특별시 서대문구 신촌동");
        generatePosts(3, 3, 3, addresses);

        List<Post> postNear = postRepository.findPost(GetPost.builder()
            .latitude(latitude).longitude(longitude).offset(offset).build());

        assertEquals(3, postNear.size());

        assertEquals(3, postNear.get(0).getFiles().size());
        assertEquals(3, postNear.get(1).getFiles().size());
        assertEquals(3, postNear.get(2).getFiles().size());

        assertEquals(3, postNear.get(0).getComments().size());
        assertEquals(3, postNear.get(1).getComments().size());
        assertEquals(3, postNear.get(2).getComments().size());

    }

    @Test
    void findPostNear_3Post_OrderbyDistance() {
        List<String> addresses = new ArrayList<>();
        addresses.add("서울특별시 종로구 세종로");
        addresses.add("서울특별시 중구 삼각동");
        addresses.add("서울특별시 마포구 성산동");
        generatePosts(3, 3, 3, addresses);

        em.flush();
        em.clear();

        double latitude = 37.59554172008826;
        double longitude = 126.96567699902982;
        int offset = 0;

        List<Post> postNear = postRepository.findPost(GetPost.builder()
            .latitude(latitude).longitude(longitude).offset(offset).build());
        assertEquals(3, postNear.size());
        assertEquals("세종로", postNear.get(0).getAddress().getEupmyundong());
        assertEquals("삼각동", postNear.get(1).getAddress().getEupmyundong());
        assertEquals("성산동", postNear.get(2).getAddress().getEupmyundong());

        postNear.stream().map(p -> p.getUser().getUsername());
        postNear.stream().map(p -> p.getFiles().getFirst().getFile().getLocation());
    }

    @Test
    void findCommentByPostId() {
        List<Post> posts = generatePosts(1, 1, 20, null);
        Long postId = posts.getFirst().getId();
        List<Comment> results = postRepository.findCommentByPostId(new GetComment(postId));
//        System.out.println(results);
    }

    @WithSettings
    static final Settings settings = Settings.create()
        .set(Keys.JPA_ENABLED, true)
        .set(Keys.BEAN_VALIDATION_ENABLED, true)
        .set(Keys.SET_BACK_REFERENCES, true)
        .lock();

    private List<Post> generatePosts(int postSize, int postFileSize, int commentSize, List<String> addresses1) {
        if (addresses1 == null) {
            addresses1 = new ArrayList<>();
            addresses1.add("서울특별시 종로구 세종로");
            addresses1.add("서울특별시 중구 삼각동");
            addresses1.add("서울특별시 마포구 성산동");
        }

        List<Post> posts = Instancio.ofList(Post.class).size(postSize)
            .ignore(field(Post::getId))
            .ignore(field(Post::getAccessList))
            .supply(field(Post::getFiles), () -> new ArrayList<>())
            .supply(field(Post::getComments), () -> new ArrayList<>())
            .create();

        List<String> addresses = new ArrayList<>(addresses1);
        posts.stream()
            .peek(p -> {
                // post address
                String[] split = addresses.removeFirst().split(" ");
                Address address = addressRepository.findBySidoAndGunguAndEupmyundong(split[0],
                    split[1], split[2]).get();
                ReflectionTestUtils.setField(p, "legalAddress", address);
            })
            .forEach(p -> {
                // post 작성자
                User user = Instancio.of(User.class)
                    .ignore(field(User::getId))
                    .supply(field(User::getComments), () -> new ArrayList<>())
                    .supply(field(User::getAddresses), () -> new HashSet<>())
                    .supply(field(User::getFiles), () -> new ArrayList<>())
                    .supply(field(User::getRelations), () -> new ArrayList<>())
                    .create();
                User savedUser = userRepository.save(user);
                ReflectionTestUtils.setField(p, "user", savedUser);

                // post file
                List<PostFile> postFiles = Instancio.ofList(PostFile.class).size(postFileSize)
                    .ignore(field(PostFile::getId))
                    .supply(field(PostFile::getPost), () -> p)
                    .supply(field(PostFile::getFile), () -> Instancio.of(File.class).create())
                    .create();
                ReflectionTestUtils.setField(p, "file", postFiles);

        });

        posts = postRepository.saveAll(posts);

        posts.forEach(p -> {
            // comment user
            User commentUser = Instancio.of(User.class)
                .ignore(field(User::getId))
                .supply(field(User::getComments), () -> new ArrayList<>())
                .supply(field(User::getAddresses), () -> new HashSet<>())
                .supply(field(User::getFiles), () -> new ArrayList<>())
                .supply(field(User::getRelations), () -> new ArrayList<>())
                .create();
            User savedCommentUser = userRepository.save(commentUser);

            // comments
            List<Comment> comments = Instancio.ofList(Comment.class).size(commentSize)
                .ignore(field(Comment::getId))
                .supply(field(Comment::getPost), () -> p)
                .supply(field(Comment::getUser), () -> savedCommentUser)
                .create();
            ReflectionTestUtils.setField(p, "comments", comments);
        });

        return postRepository.saveAll(posts);
    }


}