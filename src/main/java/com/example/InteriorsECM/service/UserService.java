package com.example.InteriorsECM.service;

import com.example.InteriorsECM.dto.UserDto;
import com.example.InteriorsECM.model.mysql.User;

import java.nio.file.AccessDeniedException;

public interface UserService {
    void registerUser(UserDto user);
    String verifyCustomer(UserDto userDto) throws AccessDeniedException;

    String verifyAdmin(UserDto userDto) throws AccessDeniedException;

    User findByEmail(String email);
    User findById(int id);
    void applyChanged(User user);

}
