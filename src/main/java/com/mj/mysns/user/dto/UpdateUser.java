package com.mj.mysns.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateUser(
    @NotBlank
    String username,

    @Size(max = 20)
    String nextUsername,

    @Min(0) @Max(200)
    Integer babyMonths,

    @Size(max = 300)
    String content,

    List<String> addresses,

    String file
) {}
