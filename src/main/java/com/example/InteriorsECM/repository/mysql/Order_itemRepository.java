package com.example.InteriorsECM.repository.mysql;

import com.example.InteriorsECM.model.mysql.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Order_itemRepository extends JpaRepository<OrderItem, Integer> {
}
