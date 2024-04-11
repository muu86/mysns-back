package com.mj.mysns.common.file;

import lombok.Builder;

@Builder
public record FileUrl(

    String raw,

    String lg,

    String md,

    String sm

) {}
