package com.example.InteriorsECM.service;

import com.example.InteriorsECM.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
public interface ProductService {
   List<ProductDto> findAllProducts();
   ProductDto findProductById(int product_id);
   List<ProductDto> searchProductsByName(String name);

    List<ProductDto> sortByPrice();

    List<ProductDto> sortByStock();
}


