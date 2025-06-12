package com.example.InteriorsECM.model.mysql;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    // Getter và Setter cho image_url
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    // Getter và Setter cho is_primary
    public boolean getIs_primary() {
        return is_primary;
    }

    public void setPrimary(boolean is_primary) {
        this.is_primary = is_primary;
    }

    // Getter và Setter cho product
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
