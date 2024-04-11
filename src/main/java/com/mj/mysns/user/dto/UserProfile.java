package com.mj.mysns.user.dto;

import com.mj.mysns.common.file.FileDto;
import com.mj.mysns.location.dto.AddressDto;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserProfile {

    private String username;

    private Integer babyMonths;

    private String content;

    private List<FileDto> files;

    private Set<AddressDto> addresses;
}
