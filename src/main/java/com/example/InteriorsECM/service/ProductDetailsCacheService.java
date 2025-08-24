package com.example.InteriorsECM.service;

import com.example.InteriorsECM.model.mysql.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductDetailsCacheService {

    @Autowired
    private RedisService redisService;

    /**
     * Cache chi tiết product vào Redis Hash
     */
    public void cacheProductDetails(Long productId, Product product) {
        String hashKey = "product:details:" + productId;
        
        // Lưu thông tin chi tiết product
        redisService.setHashValue(hashKey, "productId", product.getProduct_id());
        redisService.setHashValue(hashKey, "name", product.getName());
        redisService.setHashValue(hashKey, "price", product.getPrice());
        redisService.setHashValue(hashKey, "size", product.getSize());
        redisService.setHashValue(hashKey, "stockQuantity", product.getStock_quantity());
        redisService.setHashValue(hashKey, "isActive", product.is_active());
        redisService.setHashValue(hashKey, "material", product.getMaterial());
        redisService.setHashValue(hashKey, "discount", product.getDiscount());
        redisService.setHashValue(hashKey, "primaryImage", product.getPrimary_image());
        redisService.setHashValue(hashKey, "categoryId", product.getCategory_id());
        
        // Cache số lượng images
        if (product.getProduct_images() != null) {
            redisService.setHashValue(hashKey, "imageCount", product.getProduct_images().size());
        }
        
        // Set TTL (60 phút)
        redisService.expire(hashKey, 60, TimeUnit.MINUTES);
    }

    /**
     * Lấy chi tiết product từ cache
     */
    public ProductDetails getProductDetails(Long productId) {
        String hashKey = "product:details:" + productId;
        
        if (!redisService.hasKey(hashKey)) {
            return null;
        }
        
        ProductDetails details = new ProductDetails();
        details.setProductId((Integer) redisService.getHashValue(hashKey, "productId"));
        details.setName((String) redisService.getHashValue(hashKey, "name"));
        details.setPrice((Float) redisService.getHashValue(hashKey, "price"));
        details.setSize((String) redisService.getHashValue(hashKey, "size"));
        details.setStockQuantity((Integer) redisService.getHashValue(hashKey, "stockQuantity"));
        details.setActive((Boolean) redisService.getHashValue(hashKey, "isActive"));
        details.setMaterial((String) redisService.getHashValue(hashKey, "material"));
        details.setDiscount((Float) redisService.getHashValue(hashKey, "discount"));
        details.setPrimaryImage((String) redisService.getHashValue(hashKey, "primaryImage"));
        details.setCategoryId((Integer) redisService.getHashValue(hashKey, "categoryId"));
        details.setImageCount((Integer) redisService.getHashValue(hashKey, "imageCount"));
        
        return details;
    }

    /**
     * Cache product inventory info
     */
    public void cacheProductInventory(Long productId, int stockQuantity, boolean isActive) {
        String hashKey = "product:inventory:" + productId;
        
        redisService.setHashValue(hashKey, "stockQuantity", stockQuantity);
        redisService.setHashValue(hashKey, "isActive", isActive);
        redisService.setHashValue(hashKey, "lastUpdated", System.currentTimeMillis());
        
        // Set TTL ngắn hơn cho inventory (15 phút)
        redisService.expire(hashKey, 15, TimeUnit.MINUTES);
    }

    /**
     * Lấy thông tin inventory từ cache
     */
    public ProductInventory getProductInventory(Long productId) {
        String hashKey = "product:inventory:" + productId;
        
        if (!redisService.hasKey(hashKey)) {
            return null;
        }
        
        ProductInventory inventory = new ProductInventory();
        inventory.setStockQuantity((Integer) redisService.getHashValue(hashKey, "stockQuantity"));
        inventory.setActive((Boolean) redisService.getHashValue(hashKey, "isActive"));
        inventory.setLastUpdated((Long) redisService.getHashValue(hashKey, "lastUpdated"));
        
        return inventory;
    }

    /**
     * Cache product pricing info
     */
    public void cacheProductPricing(Long productId, float price, float discount) {
        String hashKey = "product:pricing:" + productId;
        
        redisService.setHashValue(hashKey, "price", price);
        redisService.setHashValue(hashKey, "discount", discount);
        redisService.setHashValue(hashKey, "finalPrice", price * (1 - discount / 100));
        redisService.setHashValue(hashKey, "lastUpdated", System.currentTimeMillis());
        
        // Set TTL (30 phút)
        redisService.expire(hashKey, 30, TimeUnit.MINUTES);
    }

    /**
     * Lấy thông tin pricing từ cache
     */
    public ProductPricing getProductPricing(Long productId) {
        String hashKey = "product:pricing:" + productId;
        
        if (!redisService.hasKey(hashKey)) {
            return null;
        }
        
        ProductPricing pricing = new ProductPricing();
        pricing.setPrice((Float) redisService.getHashValue(hashKey, "price"));
        pricing.setDiscount((Float) redisService.getHashValue(hashKey, "discount"));
        pricing.setFinalPrice((Float) redisService.getHashValue(hashKey, "finalPrice"));
        pricing.setLastUpdated((Long) redisService.getHashValue(hashKey, "lastUpdated"));
        
        return pricing;
    }

    /**
     * Invalidate product cache
     */
    public void invalidateProductCache(Long productId) {
        String detailsKey = "product:details:" + productId;
        String inventoryKey = "product:inventory:" + productId;
        String pricingKey = "product:pricing:" + productId;
        
        redisService.deleteKey(detailsKey);
        redisService.deleteKey(inventoryKey);
        redisService.deleteKey(pricingKey);
    }

    /**
     * Inner class để chứa thông tin chi tiết product
     */
    public static class ProductDetails {
        private Integer productId;
        private String name;
        private Float price;
        private String size;
        private Integer stockQuantity;
        private Boolean active;
        private String material;
        private Float discount;
        private String primaryImage;
        private Integer categoryId;
        private Integer imageCount;

        // Getters and Setters
        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Float getPrice() { return price; }
        public void setPrice(Float price) { this.price = price; }

        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }

        public Integer getStockQuantity() { return stockQuantity; }
        public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }

        public String getMaterial() { return material; }
        public void setMaterial(String material) { this.material = material; }

        public Float getDiscount() { return discount; }
        public void setDiscount(Float discount) { this.discount = discount; }

        public String getPrimaryImage() { return primaryImage; }
        public void setPrimaryImage(String primaryImage) { this.primaryImage = primaryImage; }

        public Integer getCategoryId() { return categoryId; }
        public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

        public Integer getImageCount() { return imageCount; }
        public void setImageCount(Integer imageCount) { this.imageCount = imageCount; }
    }

    /**
     * Inner class để chứa thông tin inventory
     */
    public static class ProductInventory {
        private Integer stockQuantity;
        private Boolean active;
        private Long lastUpdated;

        // Getters and Setters
        public Integer getStockQuantity() { return stockQuantity; }
        public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }

        public Long getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(Long lastUpdated) { this.lastUpdated = lastUpdated; }
    }

    /**
     * Inner class để chứa thông tin pricing
     */
    public static class ProductPricing {
        private Float price;
        private Float discount;
        private Float finalPrice;
        private Long lastUpdated;

        // Getters and Setters
        public Float getPrice() { return price; }
        public void setPrice(Float price) { this.price = price; }

        public Float getDiscount() { return discount; }
        public void setDiscount(Float discount) { this.discount = discount; }

        public Float getFinalPrice() { return finalPrice; }
        public void setFinalPrice(Float finalPrice) { this.finalPrice = finalPrice; }

        public Long getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(Long lastUpdated) { this.lastUpdated = lastUpdated; }
    }
}
