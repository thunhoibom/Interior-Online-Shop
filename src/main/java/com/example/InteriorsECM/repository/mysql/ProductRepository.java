package com.example.InteriorsECM.repository.mysql;

import com.example.InteriorsECM.model.mysql.Product;
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

    @Modifying
    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    List<Product> sortProductsByPrice();

    @Modifying
    @Query("SELECT p FROM Product p ORDER BY p.stock_quantity ASC")
    List<Product> sortProductsByStock();
}
