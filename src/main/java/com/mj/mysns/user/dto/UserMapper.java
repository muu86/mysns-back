package com.mj.mysns.user.dto;

import com.mj.mysns.common.file.FileManager;
import com.mj.mysns.common.file.FileMapper;
import com.mj.mysns.location.dto.AddressMapper;
import com.mj.mysns.user.controller.CreateUserPayload;
import com.mj.mysns.user.entity.Claims;
import com.mj.mysns.user.entity.User;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = { FileMapper.class, AddressMapper.class })
public interface UserMapper {

    UserProfile userToUserProfileDto(User user, @Context FileManager fileManager);

    List<UserProfile> userToUserProfileDto(List<User> user, @Context FileManager fileManager);

    Claims ToClaims(ClaimsDto claimsDto);

    ClaimsDto toClaimsDto(Claims claims);

    @Mapping(target = "issuer", source = "iss")
    @Mapping(target = "subject", source = "sub")
    ClaimsDto createUserPayloadToClaimsDto(CreateUserPayload payload);

}
