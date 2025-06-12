package com.example.InteriorsECM.converter;

import com.example.InteriorsECM.dto.ProductDTO;
import com.example.InteriorsECM.model.mysql.Product;
import com.example.InteriorsECM.model.mysql.Product_image;

import java.util.ArrayList;

public class ProductConverter {
    public static ProductDTO mapToProductDto(Product product){
        ProductDTO productDto = ProductDTO.builder()
                .product_id(product.getProduct_id())
                .name(product.getName())
                .price(product.getPrice())
                .size(product.getSize())
                .stock_quantity(product.getStock_quantity())
                .is_active(product.is_active())
                .category_id(product.getCategory_id())
                .material(product.getMaterial())
                .discount(product.getDiscount())
                .product_images(product.getProduct_images() != null ? product.getProduct_images() : new ArrayList<>())
                .primary_image(product.getPrimary_image())
                .build();
        return productDto;
    }

    public static Product mapToEntity(ProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .size(productDTO.getSize())
                .stock_quantity(productDTO.getStock_quantity())
                .is_active(productDTO.getIs_active())
                .material(productDTO.getMaterial())
                .discount(productDTO.getDiscount())
                .primary_image(productDTO.getPrimary_image())
                .product_images(productDTO.getProduct_images() != null ? productDTO.getProduct_images() : new ArrayList<>())
                .category_id(productDTO.getCategory_id())
                .build();
    }
}
