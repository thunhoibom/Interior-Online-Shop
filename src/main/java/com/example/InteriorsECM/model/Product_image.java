package com.example.InteriorsECM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_images")
public class Product_image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int image_id;
    String image_url;
    boolean is_primary;
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
}
