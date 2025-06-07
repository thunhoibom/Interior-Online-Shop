package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.service.CartItemService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartItemController {
    public CartItemService cartItemService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    public CartItemController(CartItemService cartItemService){
        this.cartItemService = cartItemService;
    }

    @PostMapping("/api/cart/update-quantity")
    public ResponseEntity<?> updateCartItemQuantity(@RequestBody UpdateQuantityRequest request) {
        try {
            cartItemService.updateCartItemQuantity(request.getItemId(), request.getQuantity());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error updating cart item quantity: {}", request, e);
            return ResponseEntity.badRequest().body("Không thể cập nhật số lượng.");
        }
    }
    @Data
    static class UpdateQuantityRequest {
        private int itemId;
        private int quantity;
    }
}
