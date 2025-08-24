package com.example.InteriorsECM.repository.timescaledb;

import com.example.InteriorsECM.model.timescaledb.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RevenueRepository extends JpaRepository<Revenue, LocalDateTime> {
    @Query(value = "SELECT time_bucket('1 day', time) AS bucket, SUM(amount) AS total " +
            "FROM revenue " +
            "WHERE time BETWEEN :startDate AND :endDate " +
            "GROUP BY bucket " +
            "ORDER BY bucket", nativeQuery = true)
    List<Object[]> getDailyRevenue(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}