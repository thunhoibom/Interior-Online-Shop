package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.service.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {
    CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }
    @GetMapping("/menu/gio-hang")
    String carts(){
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
        System.out.println(cart_id);
        if(cart_id == null){
            return "shopping-cart";
        }else{
            cartService.AddProductToCart(cart_id, product_id);
        }
        return "shopping-cart";
    }
}
