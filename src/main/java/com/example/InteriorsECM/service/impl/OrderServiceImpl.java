package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.model.mysql.Order;
import com.example.InteriorsECM.model.mysql.OrderItem;
import com.example.InteriorsECM.model.mysql.User;
import com.example.InteriorsECM.model.mysql.UserPrincipal;
import com.example.InteriorsECM.repository.mysql.OrderRepository;
import com.example.InteriorsECM.repository.mysql.Order_itemRepository;
import com.example.InteriorsECM.service.CartService;
import com.example.InteriorsECM.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    public OrderRepository orderRepository;
    public Order_itemRepository orderItemRepository;
    private CartService cartService;
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, Order_itemRepository orderItemRepository, CartService cartService){
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
    }

    @Override
    @Transactional("mySqlTransactionManager")
    public Page<Order> getOrders(int page, int size, String status, String search) {
        Pageable pageable = PageRequest.of(page, size);
        if (status != null && !status.isEmpty() && search != null && !search.trim().isEmpty()) {
            return orderRepository.findByPaymentStatusAndUsernameOrIdContaining(status, status, pageable);
        } else if (status != null && !status.isEmpty()) {
            return orderRepository.findByPaymentStatus(status, pageable);
        } else if (search != null && !search.trim().isEmpty()) {
            return orderRepository.findByUsernameOrIdContaining(search, pageable);
        } else {
            return orderRepository.findAll(pageable);
        }
    }

    @Override
    @Transactional("mySqlTransactionManager")
    public Order getOrderById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
    @Transactional("mySqlTransactionManager")
    public void updateOrder(Order order, List<OrderItem> orderItems) {
        order.setOrderItems(orderItems);
        orderItems.forEach(item -> item.setOrder(order));
        orderRepository.save(order);
    }
    @Override
    @Transactional("mySqlTransactionManager")
    public void updateOrderStatus(int id, String status) {
        Order order = getOrderById(id);
        order.setPaymentStatus(status);
        orderRepository.save(order);
    }

    @Override
    @Transactional("mySqlTransactionManager")
    public void saveOrder(Order order, List<OrderItem> orderItems) {
        order.setOrderItems(orderItems);
        orderItems.forEach(item -> item.setOrder(order));
        orderRepository.save(order);

        // Clear user's cart after successful order
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
            User user = userDetails.getUser();
            cartService.clearCart(user.getUser_id());
        }
    }



    public Double calculateTotalRevenue() {
        return orderRepository.calculateTotalRevenue();
    }

    public Double calculateRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.calculateRevenueByDateRange(startDate, endDate);
    }
}
