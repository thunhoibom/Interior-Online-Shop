package com.example.InteriorsECM.controller;


import com.example.InteriorsECM.constants.OrderConstants;
import com.example.InteriorsECM.dto.CheckoutForm;
import com.example.InteriorsECM.model.mysql.*;
import com.example.InteriorsECM.service.CartItemService;
import com.example.InteriorsECM.service.CartService;
import com.example.InteriorsECM.service.OrderService;
import com.example.InteriorsECM.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {
    public CartService cartService;
    public CartItemService cartItemService;
    public OrderService orderService;
    public UserService userService;
    @Autowired
    public OrderController(CartService cartService,
                           CartItemService cartItemService,
                           OrderService orderService,
                           UserService userService){
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/menu/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest httpServletRequest) {


        Cookie[] cookies = httpServletRequest.getCookies();
        Integer cartId = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart-id".equals(cookie.getName())) {
                    try {
                        cartId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                            model.addAttribute("order", null);
                            model.addAttribute("checkoutForm", new CheckoutForm());
                            return "/user-facilities/checkout-page";
                    }
                    break;
                }
            }
        }
        Cart cart = cartService.displayCart(cartId);
        List<CartItem> cartItems = cart != null ? cart.getCartItems() : Collections.emptyList();
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);


        model.addAttribute("checkoutForm", new CheckoutForm());
        return "/user-facilities/checkout-page";
    }
    @PostMapping("/menu/checkout")
    public String submitCheckout(@Valid @ModelAttribute("checkoutForm") CheckoutForm checkoutForm,
                                 BindingResult bindingResult,
                                 HttpServletRequest request,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("checkoutForm", checkoutForm); // Re-add order to the model
            return "/user-facilities/checkout-page";
        }
        Integer cartId = null;
        Integer userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart-id".equals(cookie.getName())) {
                    try {
                        cartId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        return "/user-facilities/checkout-page";
                    }
                }
                if ("uid".equals(cookie.getName())) {
                    try {
                        userId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        return "/user-facilities/checkout-page";
                    }
                    break;
                }
            }
        }

        Cart cart = cartService.displayCart(cartId);
        List<CartItem> cartItems = cart.getCartItems();
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = Order.builder()
                .address(checkoutForm.getAddress())
                .notes(checkoutForm.getNotes())
                .payment(checkoutForm.getPayment())
                .user(userService.findById(userId))
                .paymentStatus(OrderConstants.OrderStatus.PENDING.getValue())
                .shippingCost(20000f)
                .taxAmount((float)(totalPrice*2/100))
                .total_price(20000f + (float)(totalPrice*2/100) + (float)totalPrice)
                .build();

        System.out.println(order.getShippingCost());
        List<OrderItem> orderItems = cartItems.stream().map(
                item -> {
                    OrderItem orderItem = OrderItem.builder()
                            .product(item.getProduct())
                            .quantity(item.getQuantity())
                            .build();
                    return orderItem;
                }
        ).collect(Collectors.toList());

        orderService.saveOrder(order, orderItems);
        // Redirect to a confirmation page
        return "redirect:/menu/products";
    }

}
