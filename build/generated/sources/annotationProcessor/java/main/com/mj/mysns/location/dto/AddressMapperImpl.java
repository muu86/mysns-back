package com.mj.mysns.location.dto;

import com.mj.mysns.location.controller.AddressResult;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.user.entity.UserAddress;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-12T03:29:59+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressDto addressToAddressDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto.AddressDtoBuilder addressDto = AddressDto.builder();

        addressDto.code( address.getCode() );
        addressDto.sido( address.getSido() );
        addressDto.gungu( address.getGungu() );
        addressDto.eupmyundong( address.getEupmyundong() );
        addressDto.li( address.getLi() );

        return addressDto.build();
    }

    @Override
    public AddressDto userAddressToAddressDto(UserAddress userAddress) {
        if ( userAddress == null ) {
            return null;
        }

        AddressDto.AddressDtoBuilder addressDto = AddressDto.builder();

        addressDto.code( userAddressAddressCode( userAddress ) );
        addressDto.sido( userAddressAddressSido( userAddress ) );
        addressDto.gungu( userAddressAddressGungu( userAddress ) );
        addressDto.eupmyundong( userAddressAddressEupmyundong( userAddress ) );
        addressDto.li( userAddressAddressLi( userAddress ) );

        return addressDto.build();
    }

    @Override
    public AddressResult addressDtoToAddressResult(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        AddressResult.AddressResultBuilder addressResult = AddressResult.builder();

        addressResult.code( addressDto.code() );
        addressResult.sido( addressDto.sido() );
        addressResult.gungu( addressDto.gungu() );
        addressResult.eupmyundong( addressDto.eupmyundong() );
        addressResult.li( addressDto.li() );

        return addressResult.build();
    }

    private String userAddressAddressCode(UserAddress userAddress) {
        if ( userAddress == null ) {
            return null;
        }
        Address address = userAddress.getAddress();
        if ( address == null ) {
            return null;
        }
        String code = address.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }

    private String userAddressAddressSido(UserAddress userAddress) {
        if ( userAddress == null ) {
            return null;
        }
        Address address = userAddress.getAddress();
        if ( address == null ) {
            return null;
        }
        String sido = address.getSido();
        if ( sido == null ) {
            return null;
        }
        return sido;
    }

    private String userAddressAddressGungu(UserAddress userAddress) {
        if ( userAddress == null ) {
            return null;
        }
        Address address = userAddress.getAddress();
        if ( address == null ) {
            return null;
        }
        String gungu = address.getGungu();
        if ( gungu == null ) {
            return null;
        }
        return gungu;
    }

    private String userAddressAddressEupmyundong(UserAddress userAddress) {
        if ( userAddress == null ) {
            return null;
        }
        Address address = userAddress.getAddress();
        if ( address == null ) {
            return null;
        }
        String eupmyundong = address.getEupmyundong();
        if ( eupmyundong == null ) {
            return null;
        }
        return eupmyundong;
    }

    private String userAddressAddressLi(UserAddress userAddress) {
        if ( userAddress == null ) {
            return null;
        }
        Address address = userAddress.getAddress();
        if ( address == null ) {
            return null;
        }
        String li = address.getLi();
        if ( li == null ) {
            return null;
        }
        return li;
    }
}
