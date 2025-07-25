package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.converter.UserConverter;
import com.example.InteriorsECM.dto.UserDto;
import com.example.InteriorsECM.model.mysql.Cart;
import com.example.InteriorsECM.model.mysql.User;
import com.example.InteriorsECM.model.mysql.UserInfo;
import com.example.InteriorsECM.repository.mysql.UserRepository;
import com.example.InteriorsECM.service.JwtService;
import com.example.InteriorsECM.service.RoleService;
import com.example.InteriorsECM.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    private final ApplicationContext applicationContext;

    UserRepository userRepository;
    RoleService roleService;
    JwtService jwtService;
    AuthenticationManager authManager;
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           JwtService jwtService,
                           AuthenticationManager authManager,
                           UserDetailsServiceImpl userDetailsService,
                           ApplicationContext applicationContext){
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.applicationContext = applicationContext;
    }
    @Override
    @Transactional
    public void registerUser(UserDto userDto){
        User user = UserConverter.mapToUser(userDto);
        user.setRole(roleService.findRoleByName("ADMIN"));
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        UserInfo userInfo = UserInfo.builder()
                .phonenumber(null)
                .name(null)
                .address(null)
                .build();
        user.setUserInfo(userInfo);
        userRepository.save(user);
    }
    @Override
    public String verifyCustomer(UserDto userDto) throws AccessDeniedException {
            try{
                Authentication authentication = authManager.
                        authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
                if(authentication.isAuthenticated()){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUsername());
                    boolean hasCustomerRole = userDetails.getAuthorities().stream()
                            .anyMatch(authority -> authority.getAuthority().equals("CUSTOMER"));
                    if (hasCustomerRole) {
                        //---set Authentication---
                        UsernamePasswordAuthenticationToken authToken
                                = new UsernamePasswordAuthenticationToken(userDetails,userDto.getPassword(), userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        //-------
                        return jwtService.generateToken(userDetails.getUsername());
                    } else {
                        throw new AccessDeniedException("User does not have the CUSTOMER role");
                    }
                }
            }catch(AuthenticationException e){
                throw new BadCredentialsException("Authentication failed");
            }
            return null;
    }

    @Override
    public String verifyAdmin(UserDto userDto) throws AccessDeniedException {
        try{
            Authentication authentication = authManager.
                    authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
            if(authentication.isAuthenticated()){
                UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUsername());
                boolean hasCustomerRole = userDetails.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

                if (hasCustomerRole) {
                    return jwtService.generateToken(userDetails.getUsername());
                } else {
                    throw new AccessDeniedException("User does not have the ADMIN role");
                }
            }
        }catch(AuthenticationException e){
            throw new BadCredentialsException("Authentication failed");
        }
        return null;

    }

    @Override
    public User findByEmail(String email) {
        try{
            User user = userRepository.findUserByEmail(email).get();
            return user;
        }catch(NoSuchElementException e){
            return null;
        }
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void applyChanged(User user) {
        userRepository.save(user);
    }
}
