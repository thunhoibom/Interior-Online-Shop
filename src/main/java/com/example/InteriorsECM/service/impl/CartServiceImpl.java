package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.model.mysql.Cart;
import com.example.InteriorsECM.model.mysql.CartItem;
import com.example.InteriorsECM.model.mysql.Product;
import com.example.InteriorsECM.repository.mysql.CartItemRepository;
import com.example.InteriorsECM.repository.mysql.CartRepository;
import com.example.InteriorsECM.repository.mysql.ProductRepository;
import com.example.InteriorsECM.service.CartService;
import com.example.InteriorsECM.service.RedisService;
import com.example.InteriorsECM.constants.CacheConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartItemRepository cartItemRepository;
    RedisService redisService;
    
    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           ProductRepository productRepository,
                           CartItemRepository cartItemRepository,
                           RedisService redisService){
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.redisService = redisService;
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
        
        // Cache cart info
        cacheCartInfo(cart_id, product_id, existingCartItem.isPresent() ? 
            existingCartItem.get().getQuantity() : 1);
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
    @Override
    @Transactional("mySqlTransactionManager")
    public void clearCart(int userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));
        cart.getCartItems().clear();
        cart.setQuantity(0);
        cartRepository.save(cart);
        
        // Clear cart cache
        String cartKey = "cart:" + cart.getId();
        redisService.deleteKey(cartKey);
    }
    
    /**
     * Cache thông tin cart vào Redis Hash
     */
    private void cacheCartInfo(int cartId, int productId, int quantity) {
        String cartKey = "cart:" + cartId;
        String productKey = "product:" + productId;
        
        // Cache product quantity trong cart
        redisService.setHashValue(cartKey, productKey, quantity);
        
        // Cache cart metadata
        redisService.setHashValue(cartKey, "cartId", cartId);
        redisService.setHashValue(cartKey, "lastUpdated", System.currentTimeMillis());
        
        // Set TTL
        redisService.expire(cartKey, CacheConstants.CART_CACHE_TTL, TimeUnit.MINUTES);
    }
    
    /**
     * Lấy thông tin cart từ cache
     */
    public Integer getCachedCartItemQuantity(int cartId, int productId) {
        String cartKey = "cart:" + cartId;
        String productKey = "product:" + productId;
        
        if (redisService.hasHashKey(cartKey, productKey)) {
            return (Integer) redisService.getHashValue(cartKey, productKey);
        }
        return null;
    }

}
