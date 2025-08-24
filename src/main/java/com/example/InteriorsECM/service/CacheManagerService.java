package com.example.InteriorsECM.service;

import com.example.InteriorsECM.constants.CacheConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheManagerService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * Cache product data
     */
    public void cacheProduct(Long productId, Object product) {
        String key = CacheConstants.getProductKey(productId);
        redisService.setValue(key, product, CacheConstants.PRODUCT_CACHE_TTL, TimeUnit.MINUTES);
    }

    /**
     * Get cached product
     */
    public Object getCachedProduct(Long productId) {
        String key = CacheConstants.getProductKey(productId);
        return redisService.getValue(key);
    }

    /**
     * Cache category data
     */
    public void cacheCategory(Long categoryId, Object category) {
        String key = CacheConstants.getCategoryKey(categoryId);
        redisService.setValue(key, category, CacheConstants.CATEGORY_CACHE_TTL, TimeUnit.MINUTES);
    }

    /**
     * Get cached category
     */
    public Object getCachedCategory(Long categoryId) {
        String key = CacheConstants.getCategoryKey(categoryId);
        return redisService.getValue(key);
    }

    /**
     * Cache user data
     */
    public void cacheUser(Long userId, Object user) {
        String key = CacheConstants.getUserKey(userId);
        redisService.setValue(key, user, CacheConstants.USER_CACHE_TTL, TimeUnit.MINUTES);
    }

    /**
     * Get cached user
     */
    public Object getCachedUser(Long userId) {
        String key = CacheConstants.getUserKey(userId);
        return redisService.getValue(key);
    }

    /**
     * Cache user by email
     */
    public void cacheUserByEmail(String email, Object user) {
        String key = CacheConstants.getUserByEmailKey(email);
        redisService.setValue(key, user, CacheConstants.USER_CACHE_TTL, TimeUnit.MINUTES);
    }

    /**
     * Get cached user by email
     */
    public Object getCachedUserByEmail(String email) {
        String key = CacheConstants.getUserByEmailKey(email);
        return redisService.getValue(key);
    }

    /**
     * Cache order data
     */
    public void cacheOrder(Long orderId, Object order) {
        String key = CacheConstants.getOrderKey(orderId);
        redisService.setValue(key, order, CacheConstants.ORDER_CACHE_TTL, TimeUnit.MINUTES);
    }

    /**
     * Get cached order
     */
    public Object getCachedOrder(Long orderId) {
        String key = CacheConstants.getOrderKey(orderId);
        return redisService.getValue(key);
    }

    /**
     * Cache cart data
     */
    public void cacheCart(Long userId, Object cart) {
        String key = CacheConstants.getCartKey(userId);
        redisService.setValue(key, cart, CacheConstants.CART_CACHE_TTL, TimeUnit.MINUTES);
    }

    /**
     * Get cached cart
     */
    public Object getCachedCart(Long userId) {
        String key = CacheConstants.getCartKey(userId);
        return redisService.getValue(key);
    }

    /**
     * Invalidate product cache
     */
    public void invalidateProductCache(Long productId) {
        String key = CacheConstants.getProductKey(productId);
        redisService.deleteKey(key);
    }

    /**
     * Invalidate category cache
     */
    public void invalidateCategoryCache(Long categoryId) {
        String key = CacheConstants.getCategoryKey(categoryId);
        redisService.deleteKey(key);
    }

    /**
     * Invalidate user cache
     */
    public void invalidateUserCache(Long userId) {
        String key = CacheConstants.getUserKey(userId);
        redisService.deleteKey(key);
    }

    /**
     * Invalidate order cache
     */
    public void invalidateOrderCache(Long orderId) {
        String key = CacheConstants.getOrderKey(orderId);
        redisService.deleteKey(key);
    }

    /**
     * Invalidate cart cache
     */
    public void invalidateCartCache(Long userId) {
        String key = CacheConstants.getCartKey(userId);
        redisService.deleteKey(key);
    }

    /**
     * Clear all caches
     */
    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            cacheManager.getCache(cacheName).clear();
        });
    }

    /**
     * Clear specific cache
     */
    public void clearCache(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }

    /**
     * Check if key exists in cache
     */
    public boolean isCached(String key) {
        return redisService.hasKey(key);
    }
}
