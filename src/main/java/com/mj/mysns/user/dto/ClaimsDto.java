package com.mj.mysns.user.dto;

import lombok.Builder;

@Builder
public record ClaimsDto(
    String issuer,

    String subject,

    String first,

    String last,

    String email,

    Boolean emailVerified

    ) {
}
