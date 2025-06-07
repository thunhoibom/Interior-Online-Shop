package com.example.InteriorsECM.converter;

import com.example.InteriorsECM.dto.Product_imageDto;
import com.example.InteriorsECM.model.mysql.Product_image;

public class Product_imageConverter {
    public static Product_imageDto mapToProduct_imageDto(Product_image product_image){
        Product_imageDto product_imageDto = Product_imageDto.builder()
                .image_id(product_image.getImage_id())
                .image_url(product_image.getImage_url())
                .is_primary(product_image.is_primary())
                .build();
        return product_imageDto;
    }
}
