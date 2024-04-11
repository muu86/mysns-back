package com.mj.mysns.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mj.mysns.common.file.FileLocation;
import com.mj.mysns.common.file.FileLocation.FileLocationType;
import com.mj.mysns.common.file.FileManager;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.post.dto.CreateComment;
import com.mj.mysns.post.dto.CreatePost;
import com.mj.mysns.post.dto.PostDto;
import com.mj.mysns.post.dto.UpdateComment;
import com.mj.mysns.post.entity.Post;
import com.mj.mysns.post.exception.PostNotFoundException;
import com.mj.mysns.post.repository.PostRepository;
import com.mj.mysns.post.service.PostService;
import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.entity.UserAddress;
import com.mj.mysns.user.entity.UserAddress.Status;
import com.mj.mysns.user.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Validated
@TestMethodOrder(MethodOrderer.DisplayName.class)
class PostServiceImplTest {

    @Autowired
    PostService postService;
    @MockBean
    FileManager fileManager;
    @MockBean
    PostRepository postRepository;
    @MockBean
    UserRepository userRepository;

    @Test
    void createPost_PartialFailedFileSerivce_SaveFileOnlySuccessed() {
        List<MockMultipartFile> files = List.of(
            new MockMultipartFile("file", "test1".getBytes()),
            new MockMultipartFile("file", "test2".getBytes()));
        CreatePost dto = CreatePost.builder().username("user").content("content").files(files).build();

        User returnedUser = User.builder().username("user").build();
        when(userRepository.findByUsername(eq("user"))).thenReturn(Optional.of(returnedUser));
        when(fileManager.saveFile(any(MultipartFile.class)))
            // 하나는 성공
            .thenReturn(Optional.of(new FileLocation("success", FileLocationType.S3)))
            // 하나는 실패
            .thenReturn(Optional.empty());

        postService.createPost(dto);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        Post saved = captor.getValue();
        assertEquals(1, saved.getFiles().size());
        assertEquals("success", saved.getFiles().getFirst().getFile().getLocation().getKey());
    }

    @Test
    void createPost_EmptyUserAddress_NotSaveAddress() {
        User user = mock(User.class);

        when(user.getAddresses()).thenReturn(Collections.emptySet());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        CreatePost dto = CreatePost.builder().username("user").content("content").build();
        postService.createPost(dto);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        Post saved = captor.getValue();
        assertNull(saved.getAddress());
    }

    @Test
    void createPost_PluralUserAddress_SaveMainAddress() {
        User user = mock(User.class);
        UserAddress a1 = mock(UserAddress.class);
        UserAddress a2 = mock(UserAddress.class);

        when(a1.getStatus()).thenReturn(Status.MAIN);
        when(a1.getAddress()).thenReturn(Address.builder().code("a1").build());
        when(a2.getStatus()).thenReturn(Status.ACTIVE);
        when(a2.getAddress()).thenReturn(Address.builder().code("a2").build());
        when(user.getAddresses()).thenReturn(Set.of(a1, a2));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        CreatePost dto = CreatePost.builder().username("user").content("content").build();
        postService.createPost(dto);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        Post saved = captor.getValue();
        assertNotNull(saved.getAddress());
        assertEquals("a1", saved.getAddress().getCode());
    }

    @Test
    void createPost_InactiveAddress_NotSave() {
        User user = mock(User.class);
        UserAddress a1 = mock(UserAddress.class);
        UserAddress a2 = mock(UserAddress.class);

        when(a1.getStatus()).thenReturn(Status.INACTIVE);
        when(a1.getAddress()).thenReturn(Address.builder().code("a1").build());
        when(a2.getStatus()).thenReturn(Status.INACTIVE);
        when(a2.getAddress()).thenReturn(Address.builder().code("a2").build());
        when(user.getAddresses()).thenReturn(Set.of(a1, a2));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        CreatePost dto = CreatePost.builder().username("user").content("content").build();
        postService.createPost(dto);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        Post saved = captor.getValue();
        assertNull(saved.getAddress());
    }

    @Test
    void addComment_NotExistsPost_ThrowsPostNotFoundException() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(PostNotFoundException.class, () -> {
            postService.addComment(CreateComment.builder()
                .post(PostDto.builder().id(1L).build())
                .username("test")
                .build());
        });
    }

    @Test
    void addComment_NotExistsUser_ThrowsUserNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(PostNotFoundException.class, () -> {
            postService.addComment(CreateComment.builder()
                .post(PostDto.builder().id(1L).build())
                .username("test")
                .build());
        });
    }
    @Test
    void addComment_Valid_Success() {
        CreateComment commentDto = CreateComment.builder()
            .post(PostDto.builder().id(1L).build())
            .username("test")
            .content("test!!!")
            .build();

        Post returnedPost = Post.builder().user(User.builder().build()).build();
        User returnedUser = User.builder().build();
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(returnedPost));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(returnedUser));

        postService.addComment(commentDto);

        verify(postRepository).save(argThat(arg -> {
            assertEquals(1, arg.getComments().size());
            assertTrue(arg.getComments().stream().anyMatch(c -> c.getContent().equals("test!!!")));
            return true;
        }));
    }

    @Test
    void updateComment_NotFoundPost_ThrowsPostNotFoundException() {
        UpdateComment dto = UpdateComment.builder().id(1L).postId(1L).build();
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(PostNotFoundException.class, () -> {
            postService.updateComment(dto);
        });
    }

    @Test
    void updateComment_ValidDto_Success() {
        UpdateComment dto = UpdateComment.builder().id(1L).postId(1L).build();
        Post post = mock(Post.class);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        postService.updateComment(dto);
        verify(post, times(1)).updateComment(any());
        verify(postRepository, times(1)).save(any());
    }

//    @Test
//    @Transactional
//    void findPost() {
//        List<PostDto> postList = postService.getPostList(1);
//    }
}