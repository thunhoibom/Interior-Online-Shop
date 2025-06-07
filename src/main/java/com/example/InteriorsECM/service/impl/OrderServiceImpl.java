package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.model.mysql.Order;
import com.example.InteriorsECM.model.mysql.OrderItem;
import com.example.InteriorsECM.repository.mysql.OrderRepository;
import com.example.InteriorsECM.repository.mysql.Order_itemRepository;
import com.example.InteriorsECM.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    public OrderRepository orderRepository;
    public Order_itemRepository orderItemRepository;
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, Order_itemRepository orderItemRepository){
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional("mySqlTransactionManager")
    public void saveOrder(Order order, List<OrderItem> orderItems) {
        order.setOrderItems(orderItems);
        orderItems.forEach(item -> item.setOrder(order));

        orderRepository.save(order);
    }
}
