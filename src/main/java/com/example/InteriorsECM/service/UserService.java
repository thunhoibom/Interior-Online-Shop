package com.example.InteriorsECM.service;

import com.example.InteriorsECM.dto.UserDto;

public interface UserService {
    void registerUser(UserDto user);
    String verify(UserDto userDto);
}
