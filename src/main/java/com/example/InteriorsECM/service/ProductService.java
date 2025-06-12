package com.example.InteriorsECM.service;

import com.example.InteriorsECM.dto.ProductDTO;
import com.example.InteriorsECM.model.mysql.Product;
import java.util.List;
public interface ProductService {
   List<ProductDTO> findAllProducts();
   ProductDTO findProductById(int product_id);
   List<ProductDTO> searchProductsByName(String name);

    List<ProductDTO> sortByPrice();

    List<ProductDTO> sortByStock();

    Product createProduct(ProductDTO productDTO);
    Product updateProduct(Integer productId, ProductDTO productDTO);
    void deleteProduct(Integer productId);

}


