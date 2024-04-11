package com.mj.mysns.user.dto;

import com.mj.mysns.common.file.FileManager;
import com.mj.mysns.common.file.FileMapper;
import com.mj.mysns.location.dto.AddressDto;
import com.mj.mysns.location.dto.AddressMapper;
import com.mj.mysns.user.controller.CreateUserPayload;
import com.mj.mysns.user.entity.Claims;
import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.entity.UserAddress;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-12T03:29:59+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public UserProfile userToUserProfileDto(User user, FileManager fileManager) {
        if ( user == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder userProfile = UserProfile.builder();

        userProfile.username( user.getUsername() );
        userProfile.babyMonths( user.getBabyMonths() );
        userProfile.content( user.getContent() );
        userProfile.files( fileMapper.userFileToFileDto( user.getFiles(), fileManager ) );
        userProfile.addresses( userAddressSetToAddressDtoSet( user.getAddresses(), fileManager ) );

        return userProfile.build();
    }

    @Override
    public List<UserProfile> userToUserProfileDto(List<User> user, FileManager fileManager) {
        if ( user == null ) {
            return null;
        }

        List<UserProfile> list = new ArrayList<UserProfile>( user.size() );
        for ( User user1 : user ) {
            list.add( userToUserProfileDto( user1, fileManager ) );
        }

        return list;
    }

    @Override
    public Claims ToClaims(ClaimsDto claimsDto) {
        if ( claimsDto == null ) {
            return null;
        }

        Claims.ClaimsBuilder claims = Claims.builder();

        claims.first( claimsDto.first() );
        claims.last( claimsDto.last() );
        claims.email( claimsDto.email() );
        claims.emailVerified( claimsDto.emailVerified() );
        claims.issuer( claimsDto.issuer() );
        claims.subject( claimsDto.subject() );

        return claims.build();
    }

    @Override
    public ClaimsDto toClaimsDto(Claims claims) {
        if ( claims == null ) {
            return null;
        }

        ClaimsDto.ClaimsDtoBuilder claimsDto = ClaimsDto.builder();

        claimsDto.issuer( claims.getIssuer() );
        claimsDto.subject( claims.getSubject() );
        claimsDto.first( claims.getFirst() );
        claimsDto.last( claims.getLast() );
        claimsDto.email( claims.getEmail() );
        claimsDto.emailVerified( claims.getEmailVerified() );

        return claimsDto.build();
    }

    @Override
    public ClaimsDto createUserPayloadToClaimsDto(CreateUserPayload payload) {
        if ( payload == null ) {
            return null;
        }

        ClaimsDto.ClaimsDtoBuilder claimsDto = ClaimsDto.builder();

        claimsDto.issuer( payload.iss() );
        claimsDto.subject( payload.sub() );
        claimsDto.first( payload.first() );
        claimsDto.last( payload.last() );
        claimsDto.email( payload.email() );
        claimsDto.emailVerified( payload.emailVerified() );

        return claimsDto.build();
    }

    protected Set<AddressDto> userAddressSetToAddressDtoSet(Set<UserAddress> set, FileManager fileManager) {
        if ( set == null ) {
            return null;
        }

        Set<AddressDto> set1 = new LinkedHashSet<AddressDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( UserAddress userAddress : set ) {
            set1.add( addressMapper.userAddressToAddressDto( userAddress ) );
        }

        return set1;
    }
}
