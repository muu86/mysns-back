package com.mj.mysns.user.dto;

import com.mj.mysns.common.file.FileDto;
import com.mj.mysns.location.dto.AddressDto;
import java.util.List;
import lombok.Builder;

@Builder
public record UserDto(

    String username,

    Integer babyMonths,

    String content,

    String legalAddressCode,

    List<AddressDto> userAddresses,

    List<FileDto> files,

    ClaimsDto claims
) {

}
