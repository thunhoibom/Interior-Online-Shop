package com.example.InteriorsECM.model.mysql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="role")
@Table(name="role")
public class Role {
    @Id
    int id;
    String name;
}

