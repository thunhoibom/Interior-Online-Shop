package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.model.mysql.Cart;
import com.example.InteriorsECM.model.mysql.CartItem;
import com.example.InteriorsECM.service.CartItemService;
import com.example.InteriorsECM.service.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@Transactional("mySqlTransactionManager")
public class CartController {
    public CartService cartService;
    public CartItemService cartItemService;


    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    public CartController(CartService cartService, CartItemService cartItemService){
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }
    @GetMapping("/menu/gio-hang")
    public String displayCart(HttpServletRequest request, Model model) {
        Integer cartId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart-id".equals(cookie.getName())) {
                    try {
                        cartId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        logger.error("Invalid cart-id cookie value: {}", cookie.getValue(), e);
                        model.addAttribute("error", "ID giỏ hàng không hợp lệ.");
                        model.addAttribute("products", List.of()); // Ensure products is set
                        model.addAttribute("cartId", null);
                        return "shopping-cart";
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
        return "shopping-cart";
    }
    @PostMapping("/menu/gio-hang")
    String addProductToCart(@RequestParam(name="product_id", required = false) int product_id,
                            HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        Integer cart_id = null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("cart-id")){
                    cart_id = Integer.parseInt(cookie.getValue());
                }
            }
        }
        if(cart_id == null){
            return "redirect:/menu/gio-hang";
        }else{
            cartService.AddProductToCart(cart_id, product_id);
        }
        return "redirect:/menu/gio-hang";
    }

}
