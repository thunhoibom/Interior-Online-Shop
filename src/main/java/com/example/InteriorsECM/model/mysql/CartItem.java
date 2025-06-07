package com.example.InteriorsECM.model.mysql;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(
        name = "cart_item",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_cart_item_product_cart",
                columnNames = {"product_id", "cart_id"}
        )
)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
}
