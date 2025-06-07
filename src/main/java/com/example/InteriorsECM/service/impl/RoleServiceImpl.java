package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.model.mysql.Role;
import com.example.InteriorsECM.repository.mysql.RoleRepository;
import com.example.InteriorsECM.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    public RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name).get();
    }
}
