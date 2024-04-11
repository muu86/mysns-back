package com.mj.mysns.user.service;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mj.mysns.common.file.File.Status;
import com.mj.mysns.common.file.FileLocation;
import com.mj.mysns.common.file.FileLocation.FileLocationType;
import com.mj.mysns.common.file.FileManager;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.location.entity.Geodata;
import com.mj.mysns.location.exception.AddressNotFoundException;
import com.mj.mysns.location.repository.AddressRepository;
import com.mj.mysns.location.service.LocationService;
import com.mj.mysns.user.dto.ClaimsDto;
import com.mj.mysns.user.dto.UpdateUser;
import com.mj.mysns.user.dto.UserDto;
import com.mj.mysns.user.dto.UserProfile;
import com.mj.mysns.user.entity.Claims;
import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.entity.UserAddress;
import com.mj.mysns.user.exception.UserDuplicatedException;
import com.mj.mysns.user.exception.UserNotFoundException;
import com.mj.mysns.user.exception.UsernameDuplicatedException;
import com.mj.mysns.user.repository.UserRepository;
import com.mj.mysns.user.service.UserService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @MockBean UserRepository userRepository;
    @MockBean AddressRepository addressRepository;
    @MockBean FileManager fileManager;
    @MockBean LocationService locationService;

    @Test
    void findByIssuerAndSubject() {
    }

    @Test
    void findByAuthentication() {
    }

    @Test
    void saveUser_ExisUser_ThrowsException() {
        UserDto userDto = UserDto.builder().claims(ClaimsDto.builder()
                .issuer("test").subject("test").build())
            .username("test").legalAddressCode("1234").build();

        when(userRepository.exists(any(Example.class))).thenReturn(true);

        assertThrows(UserDuplicatedException.class, () -> userService.saveUser(userDto));
    }

    @Test
    void saveUser_ExistUsername_ThrowsException() {
        UserDto userDto = UserDto.builder().claims(ClaimsDto.builder()
                .issuer("test").subject("test").build())
            .username("test").legalAddressCode("1234").build();

        when(userRepository.exists(any(Example.class)))
            .thenReturn(false) // user Exists 는 통과
            .thenReturn(true); // targetUsername 중복은 통과 못함

        assertThrows(UsernameDuplicatedException.class, () -> userService.saveUser(userDto));
    }

    @Test
    void saveUser_NotFoundAddress_ThrowsException() {
        UserDto userDto = UserDto.builder().claims(ClaimsDto.builder()
                .issuer("test").subject("test").build())
                .username("test").legalAddressCode("1234").build();


        when(addressRepository.findByCode("1234")).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> userService.saveUser(userDto));
    }

    @Test
    void saveUser_Success() {
        UserDto userDto = UserDto.builder().claims(ClaimsDto.builder()
                .issuer("test").subject("test").build())
            .username("test").legalAddressCode("1234").build();
        User toBeReturned = User.builder().username("test").claims(Claims.builder().issuer("test").subject("test").build()).build();

        when(userRepository.exists(Example.of(User.builder()
            .claims(Claims.builder()
                .issuer("test")
                .subject("test")
                .build())
            .build()))).thenReturn(false);
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
        when(addressRepository.findByCode("1234")).thenReturn(Optional.of(Address.builder().build()));
        when(userRepository.save(any(User.class))).thenReturn(toBeReturned);

        userService.saveUser(userDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserProfileByUsername() {
        UserDto userDto = UserDto.builder().username("test").build();

        Point<G2D> point = point(WGS84,
            g(126.96567699902982, 37.59554172008826));
//        org.locationtech.jts.geom.Point point1 = JTS.to(point);
        Address address1 = Address.builder().code("1").geo(Geodata.builder().geoG(point).build()).build();
        Address address2 = Address.builder().code("2").geo(Geodata.builder().geoG(point).build()).build();

        User returnedUser = User.builder()
            .username("test")
            .babyMonths(12)
            .content("안녕하세요")
            .build();
        returnedUser.addUserAddress(UserAddress.builder()
            .user(returnedUser)
            .address(address1)
            .build());
        returnedUser.addUserAddress(UserAddress.builder()
            .user(returnedUser)
            .address(address2)
            .build());

        returnedUser.addUserFile(new FileLocation("test_1", FileLocationType.S3), Status.ACTIVE);
        returnedUser.addUserFile(new FileLocation("test_2", FileLocationType.S3), Status.INACTIVE);
        when(userRepository.findUserProfile(userDto)).thenReturn(Optional.of(returnedUser));

        Optional<UserProfile> userProfileOptional = userService.getUserProfileByUsername(
            userDto);

        assertTrue(userProfileOptional.isPresent());
        UserProfile userProfile = userProfileOptional.get();
        assertEquals(returnedUser.getUsername(), userProfile.getUsername());
        assertEquals(returnedUser.getBabyMonths(), userProfile.getBabyMonths());
        assertEquals(returnedUser.getContent(), userProfile.getContent());
        assertNotNull(userProfile.getAddresses());
        assertEquals(2, userProfile.getAddresses().size());
        assertNotNull(userProfile.getFiles());
        assertEquals(1, userProfile.getFiles().size());
    }

    @Test
    void updateUser_NotExistUser_ThrowsUserNotFoundException() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(UpdateUser.builder().username("test").build());
        });

        verify(userRepository, times(1)).findByUsername(eq("test"));
    }

    @Test
    void updateUser_ExistNextUsername_ThrowsUsernameDuplicatedException() {

        when(userRepository.findByUsername("before")).thenReturn(Optional.of(User.builder().build()));
        when(userRepository.exists(any())).thenReturn(true);

        assertThrows(UsernameDuplicatedException.class, () -> {
            userService.updateUser(UpdateUser.builder().username("before").nextUsername("after").build());
        });

        verify(userRepository, times(1)).findByUsername(eq("before"));
        verify(userRepository, times(1)).exists(argThat(arg -> {
            assertEquals("after", arg.getProbe().getUsername());
            return true;
        }));
    }

    @Test
    void updateUser_NullFile_NotCallFileManager() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(User.builder().build()));
        UpdateUser userDto = UpdateUser.builder().username("test").build();
        userService.updateUser(userDto);
        verify(fileManager, never()).saveFile(any(MultipartFile.class));
    }

    @Test
    void updateUser_FailFileManager_NotSaveFile() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(User.builder().build()));

        UpdateUser userDto = UpdateUser.builder().username("test")
            .file(new MockMultipartFile("file", "content".getBytes())).build();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        userService.updateUser(userDto);
        verify(userRepository).save(captor.capture());

        User value = captor.getValue();
        assertTrue(value.getFiles().isEmpty());
    }

    @Test
    void updateUser_SuccessFileManager_SaveFile() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(User.builder().build()));
        MockMultipartFile mockFile = new MockMultipartFile("file", "content".getBytes());
        when(fileManager.saveFile(mockFile))
            .thenReturn(Optional.of(new FileLocation("test123", FileLocationType.S3)));

        UpdateUser userDto = UpdateUser.builder().username("test")
            .file(mockFile).build();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        userService.updateUser(userDto);
        verify(userRepository).save(captor.capture());

        User value = captor.getValue();
        assertEquals(1, value.getFiles().size());
    }

    @Test
    void updateUser_NullAddresses_NotCallLocationService() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(User.builder().build()));
        UpdateUser userDto = UpdateUser.builder().username("test").build();
        userService.updateUser(userDto);
        verify(locationService, never()).getLegalAddressesByCodeIn(any());
    }

    @Test
    void updateUser_EmptyAddresses_NotCallLocationService() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(User.builder().build()));
        UpdateUser userDto = UpdateUser.builder().username("test").addresses(Collections.emptyList()).build();

        userService.updateUser(userDto);
        verify(locationService, never()).getLegalAddressesByCodeIn(any());
    }

    @Test
    void updateUser_NotFoundAddresses_ThrowsAddressNotFoundException() {
        Address mockAddress = Address.builder().code("123").build();
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(User.builder().build()));
        when(locationService.getLegalAddressesByCodeIn(List.of(mockAddress.getCode()))).thenReturn(
            Collections.emptyList());

        UpdateUser userDto = UpdateUser.builder().username("test").addresses(List.of(mockAddress.getCode())).build();
        assertThrows(AddressNotFoundException.class, () -> {
            userService.updateUser(userDto);
        });
    }

    @Test
    void updateUser_FoundAddresses_SaveAddresses() {
        Address a1 = Address.builder().code("1").build();

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(User.builder().build()));
        when(locationService.getLegalAddressesByCodeIn(List.of(a1.getCode()))).thenReturn(
            List.of(a1));

        UpdateUser userDto = UpdateUser.builder().username("test").addresses(List.of(a1.getCode())).build();
        userService.updateUser(userDto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User value = captor.getValue();
        assertNotNull(value.getAddresses());
        assertFalse(value.getAddresses().isEmpty());
        assertEquals(1, value.getAddresses().size());
    }

    @Test
    void getFriends_NullUserDto_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, () -> {
                userService.getFriends(null);
            });
    }

    @Test
    void getFriends_NullUsername_ThrowsIllegalArgumentException() {
        UserDto userDto = UserDto.builder().username(null).build();
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, () -> {
                userService.getFriends(userDto);
            });
        assertEquals("userDto.targetUsername 가 null 입니다.", ex.getMessage());
    }

    @Test
    void getFriends_BlankUsername_ThrowsIllegalArgumentException() {
        UserDto userDto = UserDto.builder().username("").build();
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, () -> {
                userService.getFriends(userDto);
            });
        assertEquals("userDto.targetUsername 가 비었습니다.", ex.getMessage());
    }

}