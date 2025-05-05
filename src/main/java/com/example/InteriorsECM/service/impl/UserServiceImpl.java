package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.converter.UserConverter;
import com.example.InteriorsECM.dto.UserDto;
import com.example.InteriorsECM.model.Role;
import com.example.InteriorsECM.model.User;
import com.example.InteriorsECM.repository.UserRepository;
import com.example.InteriorsECM.service.JwtService;
import com.example.InteriorsECM.service.RoleService;
import com.example.InteriorsECM.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleService roleService;
    JwtService jwtService;
    AuthenticationManager authManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, JwtService jwtService, AuthenticationManager authManager){
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }
    public void registerUser(UserDto userDto){
        User user = UserConverter.mapToUser(userDto);
        user.setRole(roleService.findRoleByName("CUSTOMER"));
        userRepository.save(user);
    }
    public String verify(UserDto userDto){
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(userDto.getUsername());
        }
        return null;
    }
}
