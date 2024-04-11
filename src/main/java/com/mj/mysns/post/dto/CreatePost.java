package com.mj.mysns.post.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CreatePost(

    @NotNull String username,

    @NotNull String content,

    String addressCode,

    List<String> keys,

    List<? extends MultipartFile> files
) {}
