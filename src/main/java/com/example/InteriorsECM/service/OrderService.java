package com.example.InteriorsECM.service;

import com.example.InteriorsECM.model.mysql.Order;
import com.example.InteriorsECM.model.mysql.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    void saveOrder(Order order, List<OrderItem> orderItems);
    Order getOrderById(int id);
    void updateOrderStatus(int id, String status);
    void updateOrder(Order order, List<OrderItem> orderItems);
    Page<Order> getOrders(int page, int size, String status, String search);

    Double calculateTotalRevenue();

    Double calculateRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate);

}
