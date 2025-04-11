package com.example.InteriorsECM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int product_id;
    String name;
    float price;
    String size;
    int stock_quantity;
    boolean is_active;
    String material;
    float discount;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<Product_image> product_images;
    int category_id;
}
