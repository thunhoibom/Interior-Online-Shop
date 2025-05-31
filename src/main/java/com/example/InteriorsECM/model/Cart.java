package com.example.InteriorsECM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int quantity;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToMany(mappedBy = "carts")
    List<Product> products;

    public void addProduct(Product product){
        this.products.add(product);
    }
}
