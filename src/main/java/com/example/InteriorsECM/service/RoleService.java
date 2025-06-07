package com.example.InteriorsECM.service;

import com.example.InteriorsECM.model.mysql.Role;

public interface RoleService {
    Role findRoleByName(String name);
}
