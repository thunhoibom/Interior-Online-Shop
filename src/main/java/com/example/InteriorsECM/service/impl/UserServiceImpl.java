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
import com.example.InteriorsECM.service.RedisService;
import com.example.InteriorsECM.constants.CacheConstants;
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
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    private final ApplicationContext applicationContext;

    UserRepository userRepository;
    RoleService roleService;
    JwtService jwtService;
    AuthenticationManager authManager;
    UserDetailsServiceImpl userDetailsService;
    RedisService redisService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           JwtService jwtService,
                           AuthenticationManager authManager,
                           UserDetailsServiceImpl userDetailsService,
                           ApplicationContext applicationContext,
                           RedisService redisService){
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.applicationContext = applicationContext;
        this.redisService = redisService;
    }
    @Override
    @Transactional("mySqlTransactionManager")
    public void registerUser(UserDto userDto){
        User user = UserConverter.mapToUser(userDto);
        user.setRole(roleService.findRoleByName("CUSTOMER"));
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
        // Kiểm tra cache trước
        String cacheKey = "user:email:" + email.toLowerCase();
        if (redisService.hasKey(cacheKey)) {
            Integer userId = (Integer) redisService.getHashValue(cacheKey, "userId");
            if (userId != null) {
                return findById(userId);
            }
        }
        
        try{
            User user = userRepository.findUserByEmail(email).get();
            if (user != null) {
                // Cache user info
                cacheUserInfo(user);
            }
            return user;
        }catch(NoSuchElementException e){
            return null;
        }
    }

    @Override
    public User findById(int id) {
        // Kiểm tra cache trước
        String cacheKey = "user:" + id;
        if (redisService.hasKey(cacheKey)) {
            // Lấy thông tin user từ cache
            String username = (String) redisService.getHashValue(cacheKey, "username");
            String email = (String) redisService.getHashValue(cacheKey, "email");
            String role = (String) redisService.getHashValue(cacheKey, "role");
            
            if (username != null && email != null) {
                // Tạo User object từ cache
                User user = User.builder()
                    .user_id(id)
                    .username(username)
                    .email(email)
                    .build();
                return user;
            }
        }
        
        User user = userRepository.findById(id).get();
        if (user != null) {
            // Cache user info
            cacheUserInfo(user);
        }
        return user;
    }
    
    /**
     * Cache thông tin user vào Redis Hash
     */
    private void cacheUserInfo(User user) {
        String userKey = "user:" + user.getUser_id();
        String emailKey = "user:email:" + user.getEmail().toLowerCase();
        
        // Cache user info
        redisService.setHashValue(userKey, "userId", user.getUser_id());
        redisService.setHashValue(userKey, "username", user.getUsername());
        redisService.setHashValue(userKey, "email", user.getEmail());
        redisService.setHashValue(userKey, "role", user.getRole().getName());
        
        // Cache email mapping
        redisService.setHashValue(emailKey, "userId", user.getUser_id());
        
        // Set TTL
        redisService.expire(userKey, CacheConstants.USER_CACHE_TTL, TimeUnit.MINUTES);
        redisService.expire(emailKey, CacheConstants.USER_CACHE_TTL, TimeUnit.MINUTES);
    }

    @Override
    public void applyChanged(User user) {
        userRepository.save(user);
    }
}
