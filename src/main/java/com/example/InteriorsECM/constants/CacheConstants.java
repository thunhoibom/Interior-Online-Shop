package com.example.InteriorsECM.constants;

public class CacheConstants {
    
    // Cache names
    public static final String PRODUCT_CACHE = "products";
    public static final String CATEGORY_CACHE = "categories";
    public static final String USER_CACHE = "users";
    public static final String ORDER_CACHE = "orders";
    public static final String CART_CACHE = "carts";
    public static final String SESSION_CACHE = "sessions";
    
    // Cache keys patterns
    public static final String PRODUCT_BY_ID = "product:id:";
    public static final String PRODUCT_BY_CATEGORY = "product:category:";
    public static final String PRODUCT_SEARCH = "product:search:";
    public static final String CATEGORY_BY_ID = "category:id:";
    public static final String USER_BY_ID = "user:id:";
    public static final String USER_BY_EMAIL = "user:email:";
    public static final String ORDER_BY_ID = "order:id:";
    public static final String ORDER_BY_USER = "order:user:";
    public static final String CART_BY_USER = "cart:user:";
    
    // TTL values (in minutes)
    public static final long PRODUCT_CACHE_TTL = 60; // 1 hour
    public static final long CATEGORY_CACHE_TTL = 120; // 2 hours
    public static final long USER_CACHE_TTL = 30; // 30 minutes
    public static final long ORDER_CACHE_TTL = 15; // 15 minutes
    public static final long CART_CACHE_TTL = 10; // 10 minutes
    public static final long SESSION_CACHE_TTL = 30; // 30 minutes
    
    // Cache key builders
    public static String getProductKey(Long productId) {
        return PRODUCT_BY_ID + productId;
    }
    
    public static String getProductByCategoryKey(Long categoryId) {
        return PRODUCT_BY_CATEGORY + categoryId;
    }
    
    public static String getProductSearchKey(String searchTerm) {
        return PRODUCT_SEARCH + searchTerm.toLowerCase().replace(" ", "_");
    }
    
    public static String getCategoryKey(Long categoryId) {
        return CATEGORY_BY_ID + categoryId;
    }
    
    public static String getUserKey(Long userId) {
        return USER_BY_ID + userId;
    }
    
    public static String getUserByEmailKey(String email) {
        return USER_BY_EMAIL + email.toLowerCase();
    }
    
    public static String getOrderKey(Long orderId) {
        return ORDER_BY_ID + orderId;
    }
    
    public static String getOrderByUserKey(Long userId) {
        return ORDER_BY_USER + userId;
    }
    
    public static String getCartKey(Long userId) {
        return CART_BY_USER + userId;
    }
}
