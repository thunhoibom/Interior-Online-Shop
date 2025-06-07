package com.example.InteriorsECM.config;

import com.example.InteriorsECM.model.mysql.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleConfig {
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_ADMIN = "ADMIN";

    @Bean(name = "customerRole")
    public Role customerRole() {
        Role role = new Role();
        role.setName(ROLE_CUSTOMER);
        role.setId(2);
        return role;
    }

    @Bean(name = "adminRole")
    public Role adminRole() {
        Role role = new Role();
        role.setName(ROLE_ADMIN);
        role.setId(1);
        return role;
    }
}
