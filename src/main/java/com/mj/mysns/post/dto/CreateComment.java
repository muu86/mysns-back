package com.mj.mysns.post.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateComment(

    @NotBlank
    String username,

    @Valid
    @NotNull
    PostDto post,

    String content
) {

}
