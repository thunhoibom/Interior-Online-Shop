package com.example.InteriorsECM.dto;

import com.example.InteriorsECM.model.mysql.Product_image;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductDto {
    @NotNull
    int product_id;
    @NotEmpty
    String name;
    @NotEmpty
    float price;
    @NotEmpty
    String size;
    @NotEmpty
    int stock_quantity;
    @NotEmpty
    boolean is_active;
    @NotEmpty
    String material;
    float discount;
    List<Product_image> product_images;
    @NotEmpty
    int category_id;
    String primary_image;
}
