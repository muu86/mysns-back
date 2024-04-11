package com.mj.mysns.post.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record UpdatePostPayload(

    @NotNull
    Long postId,

    @NotBlank
    String username,

    @Size(max = 300)
    String content,

    List<MultipartFile> files
) {

}
