package com.mj.mysns.user.service;

import com.mj.mysns.user.dto.UpdateUser;
import com.mj.mysns.user.dto.UserDto;
import com.mj.mysns.user.dto.UserProfile;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserDto> getUserByIssuerAndSubject(String issuer, String subject);

    List<UserProfile> getUserProfileByUsername(List<UserDto> userDtos);

    void saveUser(UserDto userDto);

    Optional<UserProfile> getUserProfileByUsername(UserDto userDto);

    void updateUser(UpdateUser userDto);

    void requestFriend(UserDto from, UserDto to);

    void acceptFriend(UserDto from, UserDto to);

    List<UserDto> getFriends(UserDto userDto);

}
