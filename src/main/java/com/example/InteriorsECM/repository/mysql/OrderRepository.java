package com.example.InteriorsECM.repository.mysql;

import com.example.InteriorsECM.model.mysql.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
