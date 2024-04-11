package com.mj.mysns.post.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AddCommentPayload(

    @NotNull
    @Positive
    Long postId,

    @NotBlank
    String username,

    @Size(max = 300)
    String content
) {

}
