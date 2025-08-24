package com.example.InteriorsECM.repository.mysql;

import com.example.InteriorsECM.model.mysql.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByPaymentStatus(String status, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.user u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR CAST(o.id AS string) LIKE CONCAT('%', :search, '%')")
    Page<Order> findByUsernameOrIdContaining(@Param("search") String search, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.user u WHERE o.paymentStatus = :status AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR CAST(o.id AS string) LIKE CONCAT('%', :search, '%'))")
    Page<Order> findByPaymentStatusAndUsernameOrIdContaining(@Param("status") String status, @Param("search") String search, Pageable pageable);


    //for analytics
    @Query("SELECT COALESCE(SUM(o.total_price), 0) FROM Order o WHERE o.paymentStatus = 'SUCCESS'")
    Double calculateTotalRevenue();

    @Query("SELECT COALESCE(SUM(o.total_price), 0) FROM Order o WHERE o.paymentStatus = 'SUCCESS' AND o.order_date BETWEEN :startDate AND :endDate")
    Double calculateRevenueByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}

