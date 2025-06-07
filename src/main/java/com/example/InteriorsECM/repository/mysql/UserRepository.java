package com.example.InteriorsECM.repository.mysql;

import com.example.InteriorsECM.model.mysql.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String name);
    Optional<User> findUserByEmail(String email);
}
