package com.example.InteriorsECM.repository;

import com.example.InteriorsECM.dto.ProductDto;
import com.example.InteriorsECM.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Modifying
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    List<Product> searchProductsByName(String name);
}
