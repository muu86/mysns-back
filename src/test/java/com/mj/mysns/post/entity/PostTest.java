package com.mj.mysns.post.entity;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mj.mysns.post.dto.UpdateComment;
import com.mj.mysns.post.entity.Post.AccessType;
import com.mj.mysns.post.exception.CommentNotFoundException;
import com.mj.mysns.post.exception.PostNotFoundException;
import com.mj.mysns.user.entity.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PostTest {

    @Mock User mockUser;
    @Mock PostFile mockPostFile;
    @Mock
    Comment mockComment;

    Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
            .user(mockUser)
            .content("Test Content")
            .files(Collections.emptyList())
            .address(null)
            .comments(new ArrayList<>())
            .status(Post.PostStatus.ACTIVE)
            .build();
    }

    @Test
    void createPost_NullUser_ThrowsIllegalArgumentException() {
        assertThrowsExactly(
            IllegalArgumentException.class,
            () -> Post.builder().content("Test Content").user(null).build(),
            "user 가 null 입니다.");
    }

    @Test
    void createPost_WithoutFiles_SuccessAndEmptyFileList() {
        Post post = Post.builder()
            .user(mockUser)
            .content("Test Content")
            .build();

        assertNotNull(post);
        assertEquals("Test Content", post.getContent());
        assertEquals(mockUser, post.getUser());
        assertNotNull(post.getFiles());
        assertTrue(post.getFiles().isEmpty());
    }

    @Test
    void createPost_WithFiles_Success() {
        Post post = Post.builder()
            .user(mockUser)
            .content("test content")
            .files(List.of(mockPostFile))
            .build();

        assertEquals(mockUser, post.getUser());
        assertEquals("test content", post.getContent());
        assertEquals(1, post.getFiles().size());
        assertEquals(mockPostFile, post.getFiles().getFirst());
    }

    @Test
    void addComment_NullUser_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            post.addComment(builder -> builder.content("test"));
        });

        assertEquals(0, post.getComments().size());
    }

    @Test
    void addComment_ValidComment_SuccessAdd() {
        when(mockUser.getId()).thenReturn(1L);
        post.addComment(builder -> builder.user(mockUser).content("test"));

        List<Comment> comments = post.getComments();
        assertEquals(1, comments.size());
        assertEquals(1L, comments.stream().findFirst().get().getUser().getId());
        assertEquals("test", comments.stream().findFirst().get().getContent());
    }

    @Test
    void updateComment_InvalidPostId_ThrowsPostNotFoundException() {
        long postId = 1L;
        long invalidId = 2L;
        long commentId = 1L;
        ReflectionTestUtils.setField(post, "id", postId);

        UpdateComment dto = UpdateComment.builder()
            .id(commentId)
            .postId(invalidId)
            .build();

        assertThrows(PostNotFoundException.class, () -> {
            post.updateComment(dto);
        });
    }

    @Test
    void updateComment_NotFoundCommment_ThrowsCommentNotFoundException() {
        long postId = 1L;
        long commentId = 1L;
        ReflectionTestUtils.setField(post, "id", postId);
        UpdateComment dto = UpdateComment.builder()
            .id(commentId)
            .postId(postId)
            .build();

        assertThrows(CommentNotFoundException.class, () -> {
            post.updateComment(dto);
        });
    }

    @Test
    void updateComment_ValidComment_SuccessUpdate() {
        long postId = 1L;
        long commentId = 1L;
        ReflectionTestUtils.setField(post, "id", postId);
        UpdateComment dto = UpdateComment.builder().id(commentId).postId(postId).content("after update").build();

        when(mockComment.getId()).thenReturn(commentId);
        post.getComments().add(mockComment);

        post.updateComment(dto);

        assertEquals(1, post.getComments().size());
        verify(mockComment, times(1)).updateFrom(any());
    }

    @Test
    void changeAccessType_Valid_Success() {
        post.changeAccessType(AccessType.ACCESS_LIST);
        assertEquals(AccessType.ACCESS_LIST, post.getAccessType());
    }

    @Test
    void createAccessList_ValidUsers_Success() {
        Set<User> users = Stream.generate(() -> User.builder().build()).limit(5)
            .collect(Collectors.toSet());

        post.createAccessList(users);

        assertEquals(5, post.getAccessList().size());
    }
}