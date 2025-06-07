package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.repository.mysql.Product_imageRepository;
import com.example.InteriorsECM.service.Product_imageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Product_imageServiceImpl implements Product_imageService {
    Product_imageRepository productImageRepository;
    @Autowired
    void Product_imageServiceImpl(Product_imageRepository productImageRepository){
        this.productImageRepository = productImageRepository;
    }
//    @Override
//    public Product_imageDto findProduct_image(int Id){
//        Product_imageDto product_imageDto = Product_imageConverter.mapToProduct_imageDto(productImageRepository.findByProduct_id(Id).get());
//        return product_imageDto;
//    }
}
