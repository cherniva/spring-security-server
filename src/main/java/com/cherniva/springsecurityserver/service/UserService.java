package com.cherniva.springsecurityserver.service;

import com.cherniva.springsecurityserver.dto.UserDto;
import com.cherniva.springsecurityserver.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    void updateUser(User user);

    User findByUsername(String username);

    List<UserDto> findAllUsers();
}
