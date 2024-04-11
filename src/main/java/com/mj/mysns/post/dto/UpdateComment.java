package com.mj.mysns.post.dto;

import com.mj.mysns.post.entity.Comment.CommentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateComment(

    @NotNull
    Long id,

    @NotNull
    Long postId,

    String content,

    CommentStatus status

) {

}
