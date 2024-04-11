package com.mj.mysns.post.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.web.reactive.function.BodyInserters.fromMultipartData;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import com.mj.mysns.post.controller.AddCommentPayload;
import com.mj.mysns.post.dto.CreateComment;
import com.mj.mysns.post.dto.CreatePost;
import com.mj.mysns.post.dto.GetPost;
import com.mj.mysns.post.dto.UpdatePost;
import com.mj.mysns.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    @MockBean
    PostService postService;

    @Autowired
    WebTestClient c;

    @Test
    void createPost_NullUsername_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("content", "test");
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.targetUsername").hasJsonPath();
    }

    @Test
    void createPost_BlankUsername_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "");
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.targetUsername").hasJsonPath();
    }

    @Test
    void createPost_1CharacterUsername_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "1");
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.targetUsername").hasJsonPath();
    }

    @Test
    void createPost_2CharacterUsername_200() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "12");
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void createPost_20MoreCharacterUsername_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "012345678901234567890");
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.targetUsername").hasJsonPath();
    }

    @Test
    void createPost_20EqualEnglishUsername_200() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "abcdeabcdeabcdeabcde");
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void createPost_20MoreEnglishUsername_200() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "abcdeabcdeabcdeabcdea");
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void createPost_20MoreKoreanUsername_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "일이삼사오육칠팔구십일이삼사오육칠팔구십일");
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.targetUsername").hasJsonPath();
    }

    @Test
    void createPost_20EqualKoreanUsername_200() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "일이삼사오육칠팔구십일이삼사오육칠팔구십");
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void createPost_301Content_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        String content = "일이 봄이 왔습니다. 꽃들이 피어나기 시작했습니다. 바람이 부드럽게 스쳐가며 따뜻한 햇살이 내립니다. 새들의 지저귀는 소리가 귀에 잔잔하게 울려 퍼집니다. 작은 곤충들도 활동을 시작했는데, 잔디밭에는 작은 벌레들이 어수선하게 움직입니다. 나무들도 싹이 트고, 새로운 잎이 나기 시작했습니다. 이른 봄이지만 이미 더운 날씨가 시작되어 사람들은 서로 땀을 닦고 있습니다. 그러나 봄은 아직 멀었습니다. 여전히 쌀쌀한 날씨가 가장 많습니다. 그래도 사람들은 봄이 오고 있다는 것에 기쁨을 느낍니다. 봄은 언제나 새로운 시작을 알리는 계절입니다.";
        assertEquals(301, content.length());
        fd.part("targetUsername", "test");
        fd.part("content", content);
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void createPost_300Content_200() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        String content = "일 봄이 왔습니다. 꽃들이 피어나기 시작했습니다. 바람이 부드럽게 스쳐가며 따뜻한 햇살이 내립니다. 새들의 지저귀는 소리가 귀에 잔잔하게 울려 퍼집니다. 작은 곤충들도 활동을 시작했는데, 잔디밭에는 작은 벌레들이 어수선하게 움직입니다. 나무들도 싹이 트고, 새로운 잎이 나기 시작했습니다. 이른 봄이지만 이미 더운 날씨가 시작되어 사람들은 서로 땀을 닦고 있습니다. 그러나 봄은 아직 멀었습니다. 여전히 쌀쌀한 날씨가 가장 많습니다. 그래도 사람들은 봄이 오고 있다는 것에 기쁨을 느낍니다. 봄은 언제나 새로운 시작을 알리는 계절입니다.";
        assertEquals(300, content.length());
        fd.part("targetUsername", "test");
        fd.part("content", content);
        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void createPost_Valid_Success() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();

        fd.part("targetUsername", "test");
        fd.part("content", "test입니다");
        fd.part("file", new MockMultipartFile("test", "test1", "text/plain", "test".getBytes()).getResource());
        fd.part("file", new MockMultipartFile("test", "test2", "text/plain", "test".getBytes()).getResource());

        c.post().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isOk();

        ArgumentCaptor<CreatePost> captor = ArgumentCaptor.forClass(
            CreatePost.class);
        verify(postService, times(1)).createPost(captor.capture());

        CreatePost value = captor.getValue();
        assertEquals("test", value.username());
        assertEquals("test입니다", value.content());
        assertEquals(2, value.files().size());
        assertEquals("test1", value.files().get(0).getOriginalFilename());
        assertEquals("test2", value.files().get(1).getOriginalFilename());
    }

    @Test
    void updatePost_NullPostId_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "test");
        fd.part("content", "test입니다");
        fd.part("file", new MockMultipartFile("test", "test1", "text/plain", "test".getBytes()).getResource());

        c.patch().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.postId").hasJsonPath();
    }

    @Test
    void updatePost_NullUsername_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("postId", 1);
//        fd.part("targetUsername", "test");
        fd.part("content", "test입니다");
        fd.part("file", new MockMultipartFile("test", "test1", "text/plain", "test".getBytes()).getResource());

        c.patch().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.targetUsername").hasJsonPath();
    }

    @Test
    void updatePost_BlankUsername_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("postId", 1);
        fd.part("targetUsername", "");
        fd.part("content", "test입니다");
        fd.part("file", new MockMultipartFile("test", "test1", "text/plain", "test".getBytes()).getResource());

        c.patch().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.targetUsername").hasJsonPath();
    }

    @Test
    void updatePost_301Content_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        String content = "일이 봄이 왔습니다. 꽃들이 피어나기 시작했습니다. 바람이 부드럽게 스쳐가며 따뜻한 햇살이 내립니다. 새들의 지저귀는 소리가 귀에 잔잔하게 울려 퍼집니다. 작은 곤충들도 활동을 시작했는데, 잔디밭에는 작은 벌레들이 어수선하게 움직입니다. 나무들도 싹이 트고, 새로운 잎이 나기 시작했습니다. 이른 봄이지만 이미 더운 날씨가 시작되어 사람들은 서로 땀을 닦고 있습니다. 그러나 봄은 아직 멀었습니다. 여전히 쌀쌀한 날씨가 가장 많습니다. 그래도 사람들은 봄이 오고 있다는 것에 기쁨을 느낍니다. 봄은 언제나 새로운 시작을 알리는 계절입니다.";
        assertEquals(301, content.length());
        fd.part("postId", 1);
        fd.part("targetUsername", "test");
        fd.part("content", content);
        fd.part("file", new MockMultipartFile("test", "test1", "text/plain", "test".getBytes()).getResource());

        c.patch().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.content").hasJsonPath();
    }

    @Test
    void updatePost_300Content_200() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        String content = "일 봄이 왔습니다. 꽃들이 피어나기 시작했습니다. 바람이 부드럽게 스쳐가며 따뜻한 햇살이 내립니다. 새들의 지저귀는 소리가 귀에 잔잔하게 울려 퍼집니다. 작은 곤충들도 활동을 시작했는데, 잔디밭에는 작은 벌레들이 어수선하게 움직입니다. 나무들도 싹이 트고, 새로운 잎이 나기 시작했습니다. 이른 봄이지만 이미 더운 날씨가 시작되어 사람들은 서로 땀을 닦고 있습니다. 그러나 봄은 아직 멀었습니다. 여전히 쌀쌀한 날씨가 가장 많습니다. 그래도 사람들은 봄이 오고 있다는 것에 기쁨을 느낍니다. 봄은 언제나 새로운 시작을 알리는 계절입니다.";
        assertEquals(300, content.length());
        fd.part("postId", 1);
        fd.part("targetUsername", "test");
        fd.part("content", content);
        fd.part("file", new MockMultipartFile("test", "test1", "text/plain", "test".getBytes()).getResource());

        c.patch().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void updatePost_Valid_Success() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();

        fd.part("postId", 1);
        fd.part("targetUsername", "test");
        fd.part("content", "test입니다");
        fd.part("file", new MockMultipartFile("test", "test1", "text/plain", "test".getBytes()).getResource());
        fd.part("file", new MockMultipartFile("test", "test2", "text/plain", "test".getBytes()).getResource());

        c.patch().uri("/post")
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isOk();

        ArgumentCaptor<UpdatePost> captor = ArgumentCaptor.forClass(
            UpdatePost.class);
        verify(postService, times(1)).updatePost(captor.capture());

        UpdatePost value = captor.getValue();
        assertEquals(1, value.postId());
        assertEquals("test", value.username());
        assertEquals("test입니다", value.content());
        assertEquals(2, value.files().size());
        assertEquals("test1", value.files().get(0).getOriginalFilename());
        assertEquals("test2", value.files().get(1).getOriginalFilename());
    }
    @Test
    void getPosts_ByLatLong_CheckServiceMethodCall() {
        c.get().uri(b -> b.path("/post")
                .queryParam("latitude", "1.0")
                .queryParam("longitude", "2.0")
                .queryParam("targetUsername", "test").build())
            .exchange()
            .expectStatus().isOk();

        ArgumentCaptor<GetPost> captor = ArgumentCaptor.forClass(
            GetPost.class);
        verify(postService).getPost(captor.capture());
        GetPost value = captor.getValue();
        assertNotNull(value);
        assertEquals(1.0, value.latitude());
        assertEquals(2.0, value.longitude());
        assertEquals("test", value.targetUsername());
    }

    @Test
    void getPosts_Default_CheckServiceMethodCall() {
        c.get().uri(b -> b.path("/post")
                .build())
            .exchange()
            .expectStatus().isOk();

        ArgumentCaptor<GetPost> captor = ArgumentCaptor.forClass(
            GetPost.class);
        verify(postService).getPost(captor.capture());
        GetPost value = captor.getValue();
        assertNotNull(value);
        assertNull(value.latitude());
        assertNull(value.longitude());
        assertNull(value.targetUsername());
        assertNull(value.addressCode());
    }


    @Test
    void addComment_NullPostId_400() {
        AddCommentPayload payload = new AddCommentPayload(null, "test", "content");

        c.post().uri("/post/comment")
            .body(fromValue(payload))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.postId").hasJsonPath();

    }

    @Test
    void addComment_MinusPostId_400() {
        AddCommentPayload payload = new AddCommentPayload(-1L, "test", "content");

        c.post().uri("/post/comment")
            .body(fromValue(payload))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.postId").hasJsonPath();

    }

    @Test
    void addComment_NullUsername_400() {
        AddCommentPayload payload = new AddCommentPayload(null, null, "content");

        c.post().uri("/post/comment")
            .body(fromValue(payload))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.targetUsername").hasJsonPath();

    }


    @Test
    void addComment_BlankUsername_400() {
        AddCommentPayload payload = new AddCommentPayload(1L, "", "content");

        c.post().uri("/post/comment")
            .body(fromValue(payload))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.targetUsername").hasJsonPath();

    }

    @Test
    void addComment_301Content_400() {
        String content = "일이 봄이 왔습니다. 꽃들이 피어나기 시작했습니다. 바람이 부드럽게 스쳐가며 따뜻한 햇살이 내립니다. 새들의 지저귀는 소리가 귀에 잔잔하게 울려 퍼집니다. 작은 곤충들도 활동을 시작했는데, 잔디밭에는 작은 벌레들이 어수선하게 움직입니다. 나무들도 싹이 트고, 새로운 잎이 나기 시작했습니다. 이른 봄이지만 이미 더운 날씨가 시작되어 사람들은 서로 땀을 닦고 있습니다. 그러나 봄은 아직 멀었습니다. 여전히 쌀쌀한 날씨가 가장 많습니다. 그래도 사람들은 봄이 오고 있다는 것에 기쁨을 느낍니다. 봄은 언제나 새로운 시작을 알리는 계절입니다.";
        assertEquals(301, content.length());
        AddCommentPayload payload = new AddCommentPayload(1L, "test", content);

        c.post().uri("/post/comment")
            .body(fromValue(payload))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.content").hasJsonPath();

    }

    @Test
    void addComment_300Content_200() {
        String content = "일 봄이 왔습니다. 꽃들이 피어나기 시작했습니다. 바람이 부드럽게 스쳐가며 따뜻한 햇살이 내립니다. 새들의 지저귀는 소리가 귀에 잔잔하게 울려 퍼집니다. 작은 곤충들도 활동을 시작했는데, 잔디밭에는 작은 벌레들이 어수선하게 움직입니다. 나무들도 싹이 트고, 새로운 잎이 나기 시작했습니다. 이른 봄이지만 이미 더운 날씨가 시작되어 사람들은 서로 땀을 닦고 있습니다. 그러나 봄은 아직 멀었습니다. 여전히 쌀쌀한 날씨가 가장 많습니다. 그래도 사람들은 봄이 오고 있다는 것에 기쁨을 느낍니다. 봄은 언제나 새로운 시작을 알리는 계절입니다.";
        assertEquals(300, content.length());
        AddCommentPayload payload = new AddCommentPayload(1L, "test", content);

        c.post().uri("/post/comment")
            .body(fromValue(payload))
            .exchange()
            .expectStatus().isOk();

    }

    @Test
    void addComment_Valid_Success() {
        AddCommentPayload payload = new AddCommentPayload(1L, "test", "content");

        c.post().uri("/post/comment")
            .body(fromValue(payload))
            .exchange()
            .expectStatus().isOk();

        ArgumentCaptor<CreateComment> captor = ArgumentCaptor.forClass(CreateComment.class);
        verify(postService, times(1)).addComment(captor.capture());

        CreateComment value = captor.getValue();
        assertEquals(1, value.post().getId());
        assertEquals("test", value.username());
        assertEquals("content", value.content());
    }

    @Test
    void getComment_NullPostId_400() {
        c.get().uri(b -> b.path("/post/comment").queryParam("offset", 5).build())
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void getComment_Valid_200() {
        c.get().uri(b -> b.path("/post/comment")
                .queryParam("postId", 1)
                .queryParam("offset", 5).build())
            .exchange()
            .expectStatus().isOk();

//        verify(postService).
    }

}