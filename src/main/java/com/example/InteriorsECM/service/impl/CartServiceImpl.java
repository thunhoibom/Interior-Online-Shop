package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.model.Cart;
import com.example.InteriorsECM.model.Product;
import com.example.InteriorsECM.repository.CartRepository;
import com.example.InteriorsECM.repository.ProductRepository;
import com.example.InteriorsECM.service.CartService;
import com.example.InteriorsECM.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository){
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public void AddProductToCart(int cart_id, int product_id ){
        Cart cart = cartRepository.findCartById(cart_id).get();
        Product product = productRepository.findById(product_id).get();

        product.addCart(cart);
        cart.addProduct(product);
        cartRepository.save(cart);
    }
}
