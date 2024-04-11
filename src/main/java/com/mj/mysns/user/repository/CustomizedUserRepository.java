package com.mj.mysns.user.repository;

import com.mj.mysns.user.dto.UserDto;
import com.mj.mysns.user.entity.User;
import java.util.List;
import java.util.Optional;

public interface CustomizedUserRepository {

    Optional<User> findUserProfile(UserDto userDto);

    List<User> findUserProfile(List<UserDto> userDto);

    List<User> findFriends(UserDto userDto);

    List<User> findFriendsRequestsFromMe(UserDto userDto);

    List<User> findFriendsRequestsToMe(UserDto userDto);

}
