package com.mj.mysns.user.controller;

import com.mj.mysns.location.controller.AddressResult;
import java.util.List;
import lombok.Builder;

@Builder
public record UserProfileResult(
    String username,

    Integer babyMonths,

    String content,

    List<String> file,

    List<AddressResult> addresses
) {}
