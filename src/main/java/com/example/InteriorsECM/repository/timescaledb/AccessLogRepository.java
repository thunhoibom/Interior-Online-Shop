package com.example.InteriorsECM.repository.timescaledb;

import com.example.InteriorsECM.model.timescaledb.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    @Query(value = "SELECT time_bucket('1 hour', timestamp) AS hour, COUNT(*) AS login_count " +
            "FROM access_logs " +
            "WHERE event_type = 'login' AND success = true " +
            "GROUP BY hour " +
            "ORDER BY hour DESC", nativeQuery = true)
    List<Object[]> findLoginsPerHour();
    @Query("SELECT a FROM AccessLog a WHERE a.timestamp BETWEEN :startTime AND :endTime")
    List<AccessLog> findByTimestampBetween(Instant startTime, Instant endTime);
}