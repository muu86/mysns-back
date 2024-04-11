package com.mj.mysns.post.dto;

import com.mj.mysns.common.file.FileDto;
import com.mj.mysns.location.dto.AddressDto;
import com.mj.mysns.user.dto.UserProfile;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostDto {

    @NotNull
    private Long id;

    private UserProfile user;

    private String content;

    private List<FileDto> files;

    private AddressDto address;

    private List<CommentDto> comments;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

}
