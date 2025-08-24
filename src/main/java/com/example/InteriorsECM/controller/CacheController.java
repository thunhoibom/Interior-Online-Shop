package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.service.CacheManagerService;
import com.example.InteriorsECM.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @Autowired
    private CacheManagerService cacheManagerService;

    @Autowired
    private RedisService redisService;

    /**
     * Clear all caches
     */
    @DeleteMapping("/clear-all")
    public ResponseEntity<Map<String, String>> clearAllCaches() {
        cacheManagerService.clearAllCaches();
        Map<String, String> response = new HashMap<>();
        response.put("message", "All caches cleared successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Clear specific cache
     */
    @DeleteMapping("/clear/{cacheName}")
    public ResponseEntity<Map<String, String>> clearCache(@PathVariable String cacheName) {
        cacheManagerService.clearCache(cacheName);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cache '" + cacheName + "' cleared successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Invalidate product cache
     */
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Map<String, String>> invalidateProductCache(@PathVariable Long productId) {
        cacheManagerService.invalidateProductCache(productId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product cache for ID " + productId + " invalidated");
        return ResponseEntity.ok(response);
    }

    /**
     * Invalidate category cache
     */
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, String>> invalidateCategoryCache(@PathVariable Long categoryId) {
        cacheManagerService.invalidateCategoryCache(categoryId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Category cache for ID " + categoryId + " invalidated");
        return ResponseEntity.ok(response);
    }

    /**
     * Invalidate user cache
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Map<String, String>> invalidateUserCache(@PathVariable Long userId) {
        cacheManagerService.invalidateUserCache(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User cache for ID " + userId + " invalidated");
        return ResponseEntity.ok(response);
    }

    /**
     * Check if key exists in cache
     */
    @GetMapping("/check/{key}")
    public ResponseEntity<Map<String, Object>> checkCacheKey(@PathVariable String key) {
        boolean exists = redisService.hasKey(key);
        Map<String, Object> response = new HashMap<>();
        response.put("key", key);
        response.put("exists", exists);
        if (exists) {
            response.put("value", redisService.getValue(key));
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Get cache statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("message", "Cache statistics retrieved successfully");
        // You can add more detailed statistics here
        return ResponseEntity.ok(stats);
    }

    /**
     * Set a test value in cache
     */
    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> setTestValue(@RequestParam String key, @RequestParam String value) {
        redisService.setValue(key, value);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test value set successfully");
        response.put("key", key);
        response.put("value", value);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a value from cache
     */
    @GetMapping("/get/{key}")
    public ResponseEntity<Map<String, Object>> getValue(@PathVariable String key) {
        Object value = redisService.getValue(key);
        Map<String, Object> response = new HashMap<>();
        response.put("key", key);
        response.put("value", value);
        response.put("exists", value != null);
        return ResponseEntity.ok(response);
    }
}
