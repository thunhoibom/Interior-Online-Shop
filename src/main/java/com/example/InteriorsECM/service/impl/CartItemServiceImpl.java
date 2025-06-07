package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.model.mysql.CartItem;
import com.example.InteriorsECM.repository.mysql.CartItemRepository;
import com.example.InteriorsECM.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemServiceImpl implements CartItemService {
    public CartItemRepository cartItemRepository;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository){
        this.cartItemRepository =cartItemRepository;
    }
    @Override
    @Transactional("mySqlTransactionManager")
    public void updateCartItemQuantity(int itemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(itemId).get();
        if(cartItem != null){
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }
}
