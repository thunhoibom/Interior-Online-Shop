package com.example.InteriorsECM.repository;

import com.example.InteriorsECM.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;

public interface UserRepository extends JpaRepository<User, Integer> {
}
