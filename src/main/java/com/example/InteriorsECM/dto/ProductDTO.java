package com.example.InteriorsECM.dto;

import com.example.InteriorsECM.model.mysql.Product_image;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @NotNull(message = "Product ID is required for updates")
    private Integer product_id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Float price;

    @NotBlank(message = "Size is required")
    @Size(max = 50, message = "Size must not exceed 50 characters")
    private String size;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stock_quantity;

    private Boolean is_active = true;

    @Size(max = 100, message = "Material must not exceed 100 characters")
    private String material;

    @PositiveOrZero(message = "Discount cannot be negative")
    private Float discount;

    @Size(max = 255, message = "Primary image URL must not exceed 255 characters")
    private String primary_image;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Integer category_id;

    private List<Product_image> product_images;
}
