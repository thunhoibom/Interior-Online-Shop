package com.example.InteriorsECM.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface AccessLogService {
    void logAccessEvent(Long userId, String eventType, String ipAddress, boolean success);
     Map<LocalDate, Long> getAccessCountByDate(Instant startTime, Instant endTime);
}
