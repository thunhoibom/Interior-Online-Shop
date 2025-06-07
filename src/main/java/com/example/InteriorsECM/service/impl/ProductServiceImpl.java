package com.example.InteriorsECM.service.impl;
import com.example.InteriorsECM.converter.ProductConverter;
import com.example.InteriorsECM.dto.ProductDto;
import com.example.InteriorsECM.model.mysql.Product;
import com.example.InteriorsECM.repository.mysql.ProductRepository;
import com.example.InteriorsECM.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional("mySqlTransactionManager")
    public ProductDto findProductById(int product_id){
        Product product = productRepository.findById(product_id).get(); // Ép kiểu int sang long nếu cần
        if (product != null) {
            product.getProduct_images().size(); // Khởi tạo lazy collection
        }
        return ProductConverter.mapToProductDto(product);
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
