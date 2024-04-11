package com.mj.mysns.post.dto;

import com.mj.mysns.post.entity.Comment.CommentStatus;
import com.mj.mysns.user.dto.UserProfile;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentDto {

    private Long id;

    private UserProfile user;

    private String content;

    private CommentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

}
