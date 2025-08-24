package com.example.InteriorsECM.service;

import com.example.InteriorsECM.model.mysql.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserSessionService {

    @Autowired
    private RedisService redisService;

    /**
     * Lưu user session vào Redis Hash
     */
    public void cacheUserSession(String sessionId, User user) {
        String hashKey = "session:" + sessionId;
        
        // Lưu thông tin user vào hash
        redisService.setHashValue(hashKey, "userId", user.getUser_id());
        redisService.setHashValue(hashKey, "username", user.getUsername());
        redisService.setHashValue(hashKey, "email", user.getEmail());
        redisService.setHashValue(hashKey, "role", user.getRole().getName());
        redisService.setHashValue(hashKey, "cartId", user.getCart().getId());
        redisService.setHashValue(hashKey, "lastAccess", System.currentTimeMillis());
        
        // Set TTL cho session (30 phút)
        redisService.expire(hashKey, 30, TimeUnit.MINUTES);
    }

    /**
     * Lấy thông tin user từ session
     */
    public UserSessionInfo getUserSession(String sessionId) {
        String hashKey = "session:" + sessionId;
        
        if (!redisService.hasKey(hashKey)) {
            return null;
        }
        
        UserSessionInfo session = new UserSessionInfo();
        session.setUserId((Integer) redisService.getHashValue(hashKey, "userId"));
        session.setUsername((String) redisService.getHashValue(hashKey, "username"));
        session.setEmail((String) redisService.getHashValue(hashKey, "email"));
        session.setRole((String) redisService.getHashValue(hashKey, "role"));
        session.setCartId((Integer) redisService.getHashValue(hashKey, "cartId"));
        session.setLastAccess((Long) redisService.getHashValue(hashKey, "lastAccess"));
        
        return session;
    }

    /**
     * Cập nhật thời gian truy cập cuối
     */
    public void updateLastAccess(String sessionId) {
        String hashKey = "session:" + sessionId;
        if (redisService.hasKey(hashKey)) {
            redisService.setHashValue(hashKey, "lastAccess", System.currentTimeMillis());
        }
    }

    /**
     * Xóa user session
     */
    public void removeUserSession(String sessionId) {
        String hashKey = "session:" + sessionId;
        redisService.deleteKey(hashKey);
    }

    /**
     * Kiểm tra session có tồn tại không
     */
    public boolean isSessionValid(String sessionId) {
        String hashKey = "session:" + sessionId;
        return redisService.hasKey(hashKey);
    }

    /**
     * Lấy thông tin cart từ session
     */
    public Integer getCartIdFromSession(String sessionId) {
        String hashKey = "session:" + sessionId;
        if (redisService.hasKey(hashKey)) {
            return (Integer) redisService.getHashValue(hashKey, "cartId");
        }
        return null;
    }

    /**
     * Inner class để chứa thông tin session
     */
    public static class UserSessionInfo {
        private Integer userId;
        private String username;
        private String email;
        private String role;
        private Integer cartId;
        private Long lastAccess;

        // Getters and Setters
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public Integer getCartId() { return cartId; }
        public void setCartId(Integer cartId) { this.cartId = cartId; }

        public Long getLastAccess() { return lastAccess; }
        public void setLastAccess(Long lastAccess) { this.lastAccess = lastAccess; }
    }
}
