package com.example.InteriorsECM.service;

import com.example.InteriorsECM.model.mysql.Order;
import com.example.InteriorsECM.model.mysql.OrderItem;

import java.util.List;

public interface OrderService {
    void saveOrder(Order order, List<OrderItem> orderItems);
}
