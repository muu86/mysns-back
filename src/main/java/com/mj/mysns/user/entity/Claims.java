package com.mj.mysns.user.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Claims {

    private String first;

    private String last;

    private String email;

    private Boolean emailVerified;

    private String issuer;

    private String subject;
}
