package com.example.InteriorsECM.converter;

import com.example.InteriorsECM.dto.ProductDto;
import com.example.InteriorsECM.model.Product;

public class ProductConverter {
    public static ProductDto mapToProductDto(Product product){
        ProductDto productDto = ProductDto.builder()
                .product_id(product.getProduct_id())
                .name(product.getName())
                .price(product.getPrice())
                .size(product.getSize())
                .stock_quantity(product.getStock_quantity())
                .is_active(product.is_active())
                .category_id(product.getCategory_id())
                .material(product.getMaterial())
                .product_images(product.getProduct_images())
                .discount(product.getDiscount())
                .build();
        return productDto;
    }
}
