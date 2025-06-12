package com.example.InteriorsECM.service.impl;
import com.example.InteriorsECM.converter.ProductConverter;
import com.example.InteriorsECM.dto.ProductDTO;
import com.example.InteriorsECM.model.mysql.Product;
import com.example.InteriorsECM.model.mysql.Product_image;
import com.example.InteriorsECM.repository.mysql.ProductRepository;
import com.example.InteriorsECM.repository.mysql.Product_imageRepository;
import com.example.InteriorsECM.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    Product_imageRepository productImageRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, Product_imageRepository productImageRepository){
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }
    @Override
    public List<ProductDTO> findAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductConverter::mapToProductDto).collect(Collectors.toList());
    }
    @Override
    @Transactional("mySqlTransactionManager")
    public ProductDTO findProductById(int product_id){
        Product product = productRepository.findById(product_id).get();
        if (product != null) {
            product.getProduct_images().size(); // Khởi tạo lazy collection
        }
        return ProductConverter.mapToProductDto(product);
    }

    @Override
    @Transactional("mySqlTransactionManager")
    public Product createProduct(ProductDTO productDTO) {
        Product product = ProductConverter.mapToEntity(productDTO);
        return productRepository.save(product);
    }

    @Override
    @Transactional("mySqlTransactionManager")
    public Product updateProduct(Integer productId, ProductDTO productDTO) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
        Product product = optionalProduct.get();
        updateEntityFromDTO(product, productDTO);
        return productRepository.save(product);
    }
    private void updateEntityFromDTO(Product product, ProductDTO productDTO) {
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setSize(productDTO.getSize());
        product.setStock_quantity(productDTO.getStock_quantity());
        product.set_active(productDTO.getIs_active());
        product.setMaterial(productDTO.getMaterial());
        product.setDiscount(productDTO.getDiscount());
        product.setPrimary_image(productDTO.getPrimary_image());
        product.setCategory_id(productDTO.getCategory_id());

        List<Product_image> existingImages = product.getProduct_images();
        existingImages.clear();

        if (productDTO.getProduct_images() != null) {
            for (Product_image image : productDTO.getProduct_images()) {
                image.setProduct(product);         // liên kết lại
                existingImages.add(image);        // thêm vào list gốc
            }
        }
    }



    @Override
    @Transactional("mySqlTransactionManager")
    public void deleteProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
    }



    @Override
    public List<ProductDTO> searchProductsByName(String name){
        List<Product> products = productRepository.searchProductsByName(name);
        return products.stream().map(product -> ProductConverter.mapToProductDto(product)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> sortByPrice() {
        List<Product> products = productRepository.sortProductsByPrice();
        return products.stream()
                .map(product -> ProductConverter.mapToProductDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> sortByStock() {
        List<Product> products = productRepository.sortProductsByStock();
        return products.stream()
                .map(product -> ProductConverter.mapToProductDto(product))
                .collect(Collectors.toList());
    }
}
class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}