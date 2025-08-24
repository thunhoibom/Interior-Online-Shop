# Hướng dẫn tích hợp Redis vào InteriorsECM

## 1. Cài đặt Redis

### Windows (sử dụng WSL hoặc Docker)

#### Option 1: Sử dụng Docker (Khuyến nghị)
```bash
# Pull Redis image
docker pull redis:latest

# Chạy Redis container
docker run --name redis-interiors -p 6379:6379 -d redis:latest

# Kiểm tra container đang chạy
docker ps
```

#### Option 2: Sử dụng WSL
```bash
# Cài đặt Redis trên WSL
sudo apt update
sudo apt install redis-server

# Khởi động Redis service
sudo systemctl start redis-server
sudo systemctl enable redis-server

# Kiểm tra Redis đang chạy
redis-cli ping
```

### Linux/Mac
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install redis-server

# macOS
brew install redis
brew services start redis
```

## 2. Cấu hình Redis trong ứng dụng

### Cấu hình cơ bản đã được thêm vào `application.properties`:
```properties
# Redis Configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=
spring.data.redis.database=0
spring.data.redis.timeout=2000ms
spring.data.redis.jedis.pool.max-active=8
spring.data.redis.jedis.pool.max-idle=8
spring.data.redis.jedis.pool.min-idle=0
spring.data.redis.jedis.pool.max-wait=-1ms

# Spring Session Redis
spring.session.store-type=redis
spring.session.redis.namespace=spring:session
spring.session.redis.flush-mode=on_save
```

## 3. Các thành phần đã được tạo

### 3.1 RedisConfig.java
- Cấu hình RedisTemplate và RedisCacheManager
- Serializer cho key và value
- TTL mặc định 30 phút

### 3.2 RedisService.java
- Service cơ bản để tương tác với Redis
- Các phương thức CRUD cho Redis
- Hỗ trợ Hash operations

### 3.3 CacheConstants.java
- Định nghĩa các cache keys và TTL
- Helper methods để tạo cache keys

### 3.4 CacheManagerService.java
- Service quản lý cache cho từng entity
- Phương thức cache và invalidate cache

### 3.5 CacheController.java
- REST API để quản lý cache
- Endpoints để clear cache và kiểm tra cache

## 4. Cách sử dụng Redis trong Service

### 4.1 Sử dụng @Cacheable annotation
```java
@Service
public class ProductService {
    
    @Cacheable(value = "products", key = "#productId")
    public Product getProductById(Long productId) {
        // Logic lấy product từ database
        return productRepository.findById(productId);
    }
    
    @CacheEvict(value = "products", key = "#productId")
    public void updateProduct(Long productId, Product product) {
        // Logic update product
        productRepository.save(product);
    }
}
```

### 4.2 Sử dụng CacheManagerService
```java
@Service
public class ProductService {
    
    @Autowired
    private CacheManagerService cacheManagerService;
    
    public Product getProductById(Long productId) {
        // Kiểm tra cache trước
        Object cachedProduct = cacheManagerService.getCachedProduct(productId);
        if (cachedProduct != null) {
            return (Product) cachedProduct;
        }
        
        // Lấy từ database nếu không có trong cache
        Product product = productRepository.findById(productId);
        if (product != null) {
            cacheManagerService.cacheProduct(productId, product);
        }
        
        return product;
    }
}
```

### 4.3 Sử dụng RedisService trực tiếp
```java
@Service
public class UserService {
    
    @Autowired
    private RedisService redisService;
    
    public void cacheUserSession(String sessionId, User user) {
        String key = "session:" + sessionId;
        redisService.setValue(key, user, 30, TimeUnit.MINUTES);
    }
    
    public User getUserFromSession(String sessionId) {
        String key = "session:" + sessionId;
        return redisService.getValue(key, User.class);
    }
}
```

## 5. API Endpoints để quản lý Cache

### 5.1 Clear tất cả cache
```http
DELETE /api/cache/clear-all
```

### 5.2 Clear cache cụ thể
```http
DELETE /api/cache/clear/{cacheName}
```

### 5.3 Invalidate product cache
```http
DELETE /api/cache/product/{productId}
```

### 5.4 Invalidate category cache
```http
DELETE /api/cache/category/{categoryId}
```

### 5.5 Invalidate user cache
```http
DELETE /api/cache/user/{userId}
```

### 5.6 Kiểm tra key trong cache
```http
GET /api/cache/check/{key}
```

### 5.7 Set test value
```http
POST /api/cache/test?key=testKey&value=testValue
```

### 5.8 Get value từ cache
```http
GET /api/cache/get/{key}
```

## 6. Best Practices

### 6.1 Cache Strategy
- **Product data**: Cache 60 phút (ít thay đổi)
- **Category data**: Cache 120 phút (rất ít thay đổi)
- **User data**: Cache 30 phút (thay đổi vừa phải)
- **Order data**: Cache 15 phút (thay đổi thường xuyên)
- **Cart data**: Cache 10 phút (thay đổi liên tục)

### 6.2 Cache Invalidation
- Invalidate cache khi có thay đổi data
- Sử dụng @CacheEvict annotation
- Clear related caches khi cần thiết

### 6.3 Error Handling
- Luôn có fallback khi Redis không available
- Log errors để debug
- Graceful degradation

## 7. Monitoring và Debug

### 7.1 Redis CLI
```bash
# Kết nối Redis CLI
redis-cli

# Xem tất cả keys
KEYS *

# Xem value của key
GET key_name

# Xem TTL của key
TTL key_name

# Xóa key
DEL key_name
```

### 7.2 Application Logs
- Enable debug logging cho Redis
- Monitor cache hit/miss ratio
- Track cache performance

## 8. Troubleshooting

### 8.1 Redis không kết nối được
- Kiểm tra Redis service đang chạy
- Kiểm tra port 6379
- Kiểm tra firewall settings

### 8.2 Serialization errors
- Đảm bảo entity classes implement Serializable
- Kiểm tra Jackson configuration
- Verify Redis serializer settings

### 8.3 Memory issues
- Monitor Redis memory usage
- Set appropriate maxmemory policy
- Configure eviction policies
