package com.mj.mysns.post.controller;

import com.mj.mysns.location.controller.AddressResult;
import com.mj.mysns.post.dto.CommentDto;
import com.mj.mysns.user.dto.UserProfile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record PostResult(

    Long id,

    UserProfile user,

    String content,

    List<String> files,

    AddressResult address,

    Set<CommentDto> comments,

    LocalDateTime createdAt,

    LocalDateTime modifiedAt
) {}
