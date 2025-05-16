package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.model.User;
import com.example.InteriorsECM.model.UserPrincipal;
import com.example.InteriorsECM.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UserRepository userRepository;
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).get();

        //this is my custom UserDetails
        //getAuthorities() :: return Collections.singleton(new SimpleGrantedAuthority(user.role.name));
        return new UserPrincipal(user);
    }
}
