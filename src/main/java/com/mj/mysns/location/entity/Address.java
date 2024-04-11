package com.mj.mysns.location.entity;

import com.mj.mysns.common.BaseEntity;
import com.mj.mysns.location.dto.AddressDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@NoArgsConstructor
@Getter
public class Address extends BaseEntity {

    @Column(unique = true)
    private String code;

    private String sido;

    private String gungu;

    private String eupmyundong;

    private String li;

    @Embedded
    private Geodata geo;

    @Embedded
    private AddressMetadata metadata;

    @Builder
    public Address(String code, String sido, String gungu, String eupmyundong, String li,
        Geodata geo,
        AddressMetadata metadata) {
        this.code = code;
        this.sido = sido;
        this.gungu = gungu;
        this.eupmyundong = eupmyundong;
        this.li = li;
        this.geo = geo;
        this.metadata = metadata;
    }

    public AddressDto toDto() {
        return AddressDto.builder()
            .code(code)
            .sido(sido)
            .gungu(gungu)
            .eupmyundong(eupmyundong)
            .li(li)
            .build();
    }
}
