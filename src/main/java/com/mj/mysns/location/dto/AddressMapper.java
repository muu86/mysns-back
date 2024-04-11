package com.mj.mysns.location.dto;

import com.mj.mysns.location.controller.AddressResult;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.user.entity.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AddressMapper {

    AddressDto addressToAddressDto(Address address);

    @Mapping(target = "code", source = "address.code")
    @Mapping(target = "sido", source = "address.sido")
    @Mapping(target = "gungu", source = "address.gungu")
    @Mapping(target = "eupmyundong", source = "address.eupmyundong")
    @Mapping(target = "li", source = "address.li")
    AddressDto userAddressToAddressDto(UserAddress userAddress);

    AddressResult addressDtoToAddressResult(AddressDto addressDto);

}
