package com.example.InteriorsECM.repository;

import com.example.InteriorsECM.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String name);
    Optional<User> findUserByEmail(String email);
}
