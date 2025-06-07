package com.example.InteriorsECM.service;

import com.example.InteriorsECM.model.mysql.Cart;

public interface CartService {
    void AddProductToCart(int cart_id, int product_id);
    Cart displayCart(int cart_id);
}
