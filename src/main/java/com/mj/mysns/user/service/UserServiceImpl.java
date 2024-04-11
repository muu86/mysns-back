package com.mj.mysns.user.service;

import com.mj.mysns.common.file.File.Status;
import com.mj.mysns.common.file.FileDto;
import com.mj.mysns.common.file.FileLocation;
import com.mj.mysns.common.file.FileLocation.FileLocationType;
import com.mj.mysns.common.file.FileManager;
import com.mj.mysns.common.file.FileMapper;
import com.mj.mysns.common.file.FileUrl;
import com.mj.mysns.location.dto.AddressDto;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.location.exception.AddressNotFoundException;
import com.mj.mysns.location.repository.AddressRepository;
import com.mj.mysns.location.service.LocationService;
import com.mj.mysns.user.dto.UpdateUser;
import com.mj.mysns.user.dto.UserDto;
import com.mj.mysns.user.dto.UserDto.UserDtoBuilder;
import com.mj.mysns.user.dto.UserMapper;
import com.mj.mysns.user.dto.UserProfile;
import com.mj.mysns.user.entity.Claims;
import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.entity.User.UserBuilder;
import com.mj.mysns.user.entity.UserAddress;
import com.mj.mysns.user.exception.UserDuplicatedException;
import com.mj.mysns.user.exception.UserNotFoundException;
import com.mj.mysns.user.exception.UsernameDuplicatedException;
import com.mj.mysns.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final FileMapper fileMapper;

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final LocationService locationService;
    private final FileManager fileManager;
    private final MessageSource ms;
    private final UserMapper userMapper;

    @Override
    public Optional<UserDto> getUserByIssuerAndSubject(String issuer, String subject) {
        Optional<User> found = userRepository.findOne(
            Example.of(User.builder()
                .claims(Claims.builder().issuer(issuer).subject(subject).build())
                .build()));
        if (found.isEmpty()) return Optional.empty();

        User user = found.get();
        return Optional.of(UserDto.builder()
            .username(user.getUsername())
            .claims(userMapper.toClaimsDto(user.getClaims()))
            .build());
    }

    @Override
    public Optional<UserProfile> getUserProfileByUsername(UserDto userDto) {
        Optional<User> userOptional = userRepository.findUserProfile(userDto);
        if (userOptional.isEmpty()) return Optional.empty();

        User user = userOptional.get();
        UserProfile userProfile = user.toUserProfile();
        setFileUrl(userProfile);

        return Optional.of(userProfile);
    }

    @Override
    public List<UserProfile> getUserProfileByUsername(List<UserDto> userDtos) {
        List<User> users = userRepository.findUserProfile(userDtos);
        List<UserProfile> userProfiles = users.stream().map(User::toUserProfile).toList();
        userProfiles.forEach(this::setFileUrl);
        return userProfiles;
    }

    @Override
    @Transactional
    public void saveUser(UserDto userDto) {
        checkUserExists(userDto);
        checkUsernameExists(userDto.username());

        UserBuilder builder = User.builder();
        builder
            .username(userDto.username())
            .claims(userMapper.ToClaims(userDto.claims()))
            .babyMonths(userDto.babyMonths());

        User toSave = builder.build();

        addUserAddress(toSave, userDto.legalAddressCode());

        User saved = userRepository.save(toSave);

        log.info("user 저장, targetUsername: {}, id: {}", saved.getUsername(), saved.getId());
    }

    @Override
    @Transactional
    public void updateUser(UpdateUser dto) {
        User user = userRepository.findByUsername(dto.username())
            .orElseThrow(UserNotFoundException::new);

        user.setContent(dto.content());

        user.setBabyMonths(dto.babyMonths());

        if (dto.nextUsername() != null && !dto.nextUsername().isBlank()) {
            checkUsernameExists(dto.nextUsername());
            user.setUsername(dto.nextUsername());
        }

        setFile(user, dto);

        setAddress(user, dto);

        userRepository.save(user);

        log.info("user 업데이트, targetUsername: {}, userId : {}", user.getUsername(), user.getId());
    }

    @Override
    public void requestFriend(UserDto from, UserDto to) {
        Assert.notNull(from, ms.getMessage("Null", new Object[] { "userDto" }, Locale.getDefault()));
        Assert.notNull(to, ms.getMessage("Null", new Object[] { "userDto" }, Locale.getDefault()));
        Assert.isTrue(!from.username().isBlank(), ms.getMessage("Blank", new Object[] { "userDto.targetUsername" }, Locale.getDefault()));
        Assert.isTrue(!to.username().isBlank(), ms.getMessage("Blank", new Object[] { "userDto.targetUsername" }, Locale.getDefault()));

        User f = userRepository.findByUsername(from.username())
            .orElseThrow(UserNotFoundException::new);

        User t = userRepository.findByUsername(to.username())
            .orElseThrow(UserNotFoundException::new);

        f.requestFriend(t);
    }

    @Override
    public void acceptFriend(UserDto from, UserDto to) {
        Assert.notNull(from, ms.getMessage("Null", new Object[] { "userDto" }, Locale.getDefault()));
        Assert.notNull(to, ms.getMessage("Null", new Object[] { "userDto" }, Locale.getDefault()));
        Assert.isTrue(!from.username().isBlank(), ms.getMessage("Blank", new Object[] { "userDto.targetUsername" }, Locale.getDefault()));
        Assert.isTrue(!to.username().isBlank(), ms.getMessage("Blank", new Object[] { "userDto.targetUsername" }, Locale.getDefault()));

        User f = userRepository.findByUsername(from.username())
            .orElseThrow(UserNotFoundException::new);

        User t = userRepository.findByUsername(to.username())
            .orElseThrow(UserNotFoundException::new);

        t.acceptFriend(f);
    }

    @Override
    public List<UserDto> getFriends(UserDto userDto) {
        Assert.notNull(userDto, ms.getMessage("Null", new Object[] { "userDto" }, Locale.getDefault()));

        Assert.notNull(userDto.username(), ms.getMessage("Null", new Object[] { "userDto.targetUsername" }, Locale.getDefault()));
        Assert.isTrue(!userDto.username().isBlank(), ms.getMessage("Blank", new Object[] { "userDto.targetUsername" }, Locale.getDefault()));

        List<User> friends = userRepository.findFriends(userDto);

        List<UserDto> result = new ArrayList<>();
        for (User f : friends) {
            UserDtoBuilder builder = UserDto.builder();
            builder
                .username(f.getUsername())
                .babyMonths(f.getBabyMonths());

            // file
            List<FileDto> files = f.getFiles().stream()
                .filter(uf -> uf.getFile().getStatus().equals(Status.ACTIVE))
                .map(uf -> uf.getFile().getLocation())
                .map(fileManager::getFileUrl)
                .map(url -> FileDto.builder().url(url).build())
                .toList();
            builder.files(files);

            // address
            List<AddressDto> addresses = f.getAddresses().stream()
                .map(UserAddress::getAddress)
                .map(Address::toDto)
                .toList();
            builder.userAddresses(addresses);

            result.add(builder.build());
        }

        return result;
    }

    private void setFileUrl(UserProfile user) {
        user.getFiles()
            .forEach(f -> {
            FileUrl url = fileManager.getFileUrl(f.getLocation());
            f.setUrl(url);
        });
    }

    private void addUserAddress(User user, String legalAddressCode) {
        if (legalAddressCode == null) return;

        Address found = addressRepository.findByCode(legalAddressCode)
            .orElseThrow(AddressNotFoundException::new);

        user.addUserAddress(UserAddress.builder()
            .user(user)
            .address(found)
            .build());
    }

    private void checkUsernameExists(String username) {
        if (userRepository.exists(Example.of(User.builder()
            .username(username).build()))) {
            throw new UsernameDuplicatedException();
        }
    }

    private void checkUserExists(UserDto userDto) {
        if (userRepository.exists(Example.of(User.builder()
            .claims(Claims.builder().issuer(userDto.claims().issuer()).subject(userDto.claims()
                .subject()).build())
            .build()))) {
            throw new UserDuplicatedException();
        }
    }

    private void setAddress(User user, UpdateUser userDto) {
        if (userDto.addresses() == null || userDto.addresses().isEmpty()) return;

        List<Address> addresses = locationService.getLegalAddressesByCodeIn(userDto.addresses());
        if (addresses.isEmpty()) throw new AddressNotFoundException();

        for (Address address : addresses) {
            user.addUserAddress(UserAddress.builder()
                .user(user)
                .address(address)
                .build());
        }
    }

    private void setFile(User user, UpdateUser dto) {
        if (dto.file() == null) return;

        // 지금 올리는 파일만 active 로 처리하고 나머지 파일들은 inactive 처리
//        fileManager.saveFile(userDto.file()).ifPresent((fileLocation) -> {
//            user.getFiles().forEach(f -> f.getFile().setStatus(Status.INACTIVE));
//            user.addUserFile(fileLocation, Status.ACTIVE);
//        });
        user.getFiles().forEach(f -> f.getFile().setStatus(Status.INACTIVE));
        user.addUserFile(new FileLocation(dto.file(), FileLocationType.S3), Status.ACTIVE);
    }
}
