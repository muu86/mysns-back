package com.mj.mysns.post.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreatePostPayload(

    @NotNull
    @Size(min = 2, max = 20)
    String username,

    @Size(max = 300)
    String content,

    List<String> files
) {
}
