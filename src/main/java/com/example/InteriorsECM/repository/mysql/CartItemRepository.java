package com.example.InteriorsECM.repository.mysql;

import com.example.InteriorsECM.model.mysql.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.product_id = :productId")
    Optional<CartItem> findByCartIdAndProductId(@Param("cartId") int cartId, @Param("productId") int productId);
}
