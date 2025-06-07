package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.model.mysql.Cart;
import com.example.InteriorsECM.model.mysql.CartItem;
import com.example.InteriorsECM.model.mysql.Product;
import com.example.InteriorsECM.repository.mysql.CartItemRepository;
import com.example.InteriorsECM.repository.mysql.CartRepository;
import com.example.InteriorsECM.repository.mysql.ProductRepository;
import com.example.InteriorsECM.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartItemRepository cartItemRepository;
    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           ProductRepository productRepository,
                           CartItemRepository cartItemRepository){
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }
    @Override
    @Transactional("mySqlTransactionManager")
    public void AddProductToCart(int cart_id, int product_id ){
        Cart cart = cartRepository.findCartById(cart_id).get();
        Product product = productRepository.findById(product_id).get();
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(cart_id, product_id);
        if(existingCartItem.isPresent()){
            int temp_quantity = existingCartItem.get().getQuantity();
            existingCartItem.get().setQuantity(temp_quantity + 1);
            cartItemRepository.save(existingCartItem.get());
        }else{
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    @Transactional("mySqlTransactionManager")
    public Cart displayCart(int cart_id) {
        Cart cart = cartRepository.findCartById(cart_id).get();
        if (cart != null) {
            cart.getCartItems().size(); // Khởi tạo lazy collection trong giao dịch
        }
        return cart;
    }


}
