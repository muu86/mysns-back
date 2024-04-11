package com.mj.mysns.user.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserPayload(

    @NotBlank
    String sub,

    @NotBlank
    String iss,

    @NotBlank
    String first,

    @NotBlank
    String last,

    @Email @NotBlank
    String email,

    Boolean emailVerified,

    @NotBlank(message = "{NotBlank.CreateUserPayload.targetUsername}")
    @Size(min = 2, max = 20)
    String username,

    @Min(value = 0, message = "{Min.CreateUserPayload.babyMonths}")
    Integer babyMonths,

    @NotBlank
    String legalAddressCode

) {

}
