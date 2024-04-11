package com.mj.mysns.location.controller;

import lombok.Builder;

@Builder
public record AddressResult(
    String code,

    String sido,

    String gungu,

    String eupmyundong,

    String li

) {

}
