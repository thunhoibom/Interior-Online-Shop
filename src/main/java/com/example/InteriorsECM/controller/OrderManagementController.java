package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.model.mysql.Order;
import com.example.InteriorsECM.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/don-hang")
public class OrderManagementController {

    private final OrderService orderService;

    @Autowired
    public OrderManagementController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "search", required = false) String search) {
        Page<Order> orders = orderService.getOrders(page, size, status, search);
        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("status", status);
        model.addAttribute("search", search);
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable int id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/orders/view";
    }

    @PostMapping("/{id}/update-status")
    public String updateOrderStatus(@PathVariable int id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/don-hang/" + id;
    }
}
