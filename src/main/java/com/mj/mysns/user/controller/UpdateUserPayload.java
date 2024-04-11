package com.mj.mysns.user.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserPayload(

    @NotBlank(message = "{NotBlank.UpdateUserPayload.targetUsername}")
    String username,

    @Size(max = 20)
    String nextUsername,

    Integer babyAge,

    String content,

    // 콤마 로 구분된 주소 코드들
    String addresses,

    String file
) {}
