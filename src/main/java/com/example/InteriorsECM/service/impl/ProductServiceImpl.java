package com.example.InteriorsECM.service.impl;
import com.example.InteriorsECM.converter.ProductConverter;
import com.example.InteriorsECM.dto.ProductDto;
import com.example.InteriorsECM.model.Product;
import com.example.InteriorsECM.repository.ProductRepository;
import com.example.InteriorsECM.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @Override
    public List<ProductDto> findAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductConverter::mapToProductDto).collect(Collectors.toList());
    }
    @Override
    public ProductDto findProductById(int product_id){
        return ProductConverter.mapToProductDto(productRepository.findById(product_id).get());
    }
    @Override
    public List<ProductDto> searchProductsByName(String name){
        List<Product> products = productRepository.searchProductsByName(name);
        return products.stream().map(product -> ProductConverter.mapToProductDto(product)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> sortByPrice() {
        List<Product> products = productRepository.sortProductsByPrice();
        return products.stream()
                .map(product -> ProductConverter.mapToProductDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> sortByStock() {
        List<Product> products = productRepository.sortProductsByStock();
        return products.stream()
                .map(product -> ProductConverter.mapToProductDto(product))
                .collect(Collectors.toList());
    }
}
