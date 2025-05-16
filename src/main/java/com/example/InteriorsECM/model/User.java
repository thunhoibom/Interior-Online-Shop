package com.example.InteriorsECM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int user_id;
    String username;
    String password_hash;
    String email;
    @ManyToOne
    @JoinColumn(name="role_id")
    Role role;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
            @JoinColumn(name = "userInfoId")
    UserInfo userInfo;
}
