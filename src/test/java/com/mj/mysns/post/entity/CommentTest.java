package com.mj.mysns.post.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mj.mysns.post.dto.UpdateComment;
import com.mj.mysns.post.entity.Comment.CommentStatus;
import com.mj.mysns.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CommentTest {

    @Mock User user;
    @Mock Post post;

    @Test
    void constructor_NullPost_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Comment comment = Comment.builder()
                .user(user)
                .content("before update")
                .status(CommentStatus.ACTIVE).build();
        });
    }

    @Test
    void constructor_NullUser_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Comment comment = Comment.builder()
                .post(post)
                .content("before update")
                .status(CommentStatus.ACTIVE).build();
        });
    }

    @Test
    void update_NotEqualId_Fail() {
        UpdateComment dto = UpdateComment.builder().id(1L).content("after update").status(CommentStatus.INACTIVE).build();
        Comment comment = Comment.builder().post(post).user(user).content("before update").status(CommentStatus.ACTIVE).build();
        ReflectionTestUtils.setField(comment, "id", 1L);

        comment.updateFrom(dto);

        assertEquals("before update", comment.getContent());
        assertEquals(CommentStatus.ACTIVE, comment.getStatus());
    }

    @Test
    void update_ValidDto_SuccessUpdate() {
        UpdateComment dto = UpdateComment.builder().id(1L).content("after update").status(CommentStatus.INACTIVE).build();
        Comment comment = Comment.builder().post(post).user(user).content("before update").status(CommentStatus.ACTIVE).build();
        ReflectionTestUtils.setField(comment, "id", 2L);

        comment.updateFrom(dto);

        assertEquals("after update", comment.getContent());
        assertEquals(CommentStatus.INACTIVE, comment.getStatus());
    }

    @Test
    void equals_SameId_Equal() {
        Comment c1 = Comment.builder().post(post).user(user).content("same").status(CommentStatus.ACTIVE).build();
        Comment c2 = Comment.builder().post(post).user(user).content("same").status(CommentStatus.ACTIVE).build();
        ReflectionTestUtils.setField(c1, "id", 1L);
        ReflectionTestUtils.setField(c2, "id", 1L);

        assertEquals(c1, c2);
    }


    @Test
    void equals_DifferentId_NotEqual() {
        Comment c1 = Comment.builder().post(post).user(user).content("same").status(CommentStatus.ACTIVE).build();
        Comment c2 = Comment.builder().post(post).user(user).content("same").status(CommentStatus.ACTIVE).build();
        ReflectionTestUtils.setField(c1, "id", 1L);
        ReflectionTestUtils.setField(c2, "id", 2L);

        assertNotEquals(c1, c2);
    }

    @Test
    void equals_BothNullId_NotEqual() {
        Comment c1 = Comment.builder().post(post).user(user).content("same").status(CommentStatus.ACTIVE).build();
        Comment c2 = Comment.builder().post(post).user(user).content("same").status(CommentStatus.ACTIVE).build();

        assertNotEquals(c1, c2);
    }


    @Test
    void equals_CallerNullId_NotEqual() {
        Comment c1 = Comment.builder().post(post).user(user).content("same").status(CommentStatus.ACTIVE).build();
        Comment c2 = Comment.builder().post(post).user(user).content("same").status(CommentStatus.ACTIVE).build();
        ReflectionTestUtils.setField(c2, "id", 1L);

        assertNotEquals(c1, c2);
    }


    @Test
    void equals_CalleeNullId_NotEqual() {
        Comment c1 = Comment.builder().post(post).user(user).content("same").status(CommentStatus.ACTIVE).build();
        Comment c2 = Comment.builder().post(post).user(user).content("same").status(CommentStatus.ACTIVE).build();
        ReflectionTestUtils.setField(c1, "id", 1L);

        assertNotEquals(c1, c2);
    }
}