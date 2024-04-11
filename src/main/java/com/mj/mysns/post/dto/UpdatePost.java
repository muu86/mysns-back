package com.mj.mysns.post.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UpdatePost(

    @NotNull Long postId,

    @NotNull String username,

    Integer babyMonth,

    String content,

//    User user,

    List<MultipartFile> files
) {
}
