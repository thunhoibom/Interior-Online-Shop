package com.example.InteriorsECM.repository;

import com.example.InteriorsECM.model.Product_image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface Product_imageRepository extends JpaRepository<Product_image, Integer> {
}
