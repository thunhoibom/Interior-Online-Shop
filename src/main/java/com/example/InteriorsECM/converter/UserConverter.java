package com.example.InteriorsECM.converter;

import com.example.InteriorsECM.dto.UserDto;
import com.example.InteriorsECM.model.Customer;
import com.example.InteriorsECM.model.Role;
import com.example.InteriorsECM.model.User;
import com.example.InteriorsECM.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserConverter {
//    @Autowired
//    @Qualifier("customerRole")
//    Role role;


    public static User mapToUser(UserDto userDto){
        User user = User.builder()
                .username(userDto.getUsername())
                .password_hash((new BCryptPasswordEncoder(12)).encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .customer(new Customer())
                .build();
        return user;
    }
}
