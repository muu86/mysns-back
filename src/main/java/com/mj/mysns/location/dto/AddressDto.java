package com.mj.mysns.location.dto;

import lombok.Builder;

@Builder
public record AddressDto(

    String code,

    String sido,

    String gungu,

    String eupmyundong,

    String li
) {}
