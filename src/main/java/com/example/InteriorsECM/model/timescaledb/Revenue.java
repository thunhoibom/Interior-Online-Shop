package com.example.InteriorsECM.model.timescaledb;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "revenue")
public class Revenue {
    @Id
    private LocalDateTime time;
    private Double amount;
}