package com.example.InteriorsECM.converter;

import com.example.InteriorsECM.dto.UserDto;
import com.example.InteriorsECM.model.mysql.User;
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
                .build();
        return user;
    }
}
