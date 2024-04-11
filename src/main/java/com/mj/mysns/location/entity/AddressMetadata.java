package com.mj.mysns.location.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AddressMetadata {

    // 순위?? 가 뭔지 모름..
    private String sunwi;

    // 법정동 데이터에 존재하는 날짜들
    private LocalDateTime metaCreatedAt;

    private LocalDateTime metaModifiedAt;

    private LocalDateTime metaDeletedAt;

    private String prevCode;
}
