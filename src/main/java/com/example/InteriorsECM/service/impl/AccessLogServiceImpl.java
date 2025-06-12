package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.model.timescaledb.AccessLog;
import com.example.InteriorsECM.repository.timescaledb.AccessLogRepository;
import com.example.InteriorsECM.service.AccessLogService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccessLogServiceImpl implements AccessLogService {
    public AccessLogRepository accessLogRepository;
    @Autowired
    public AccessLogServiceImpl(AccessLogRepository accessLogRepository){
        this.accessLogRepository = accessLogRepository;
    }
    @Override
    @Transactional("timescaleTransactionManager")
    public void logAccessEvent(Long userId, String eventType, String ipAddress, boolean success) {
        AccessLog log = new AccessLog();
        log.setTimestamp(Instant.now());
        log.setUserId(userId != null ? userId : 0L); // Use 0 for unauthenticated users
        log.setEventType(eventType);
        log.setIpAddress(ipAddress);
        log.setSuccess(success);

        try {
            accessLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Error saving log: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Transactional("timescaleTransactionManager")
    @Override
    public Map<LocalDate, Long> getAccessCountByDate(Instant startTime, Instant endTime) {

        // Chuyển endTime để bao gồm cả thời điểm cuối
        endTime = endTime.plusMillis(1);
        List<AccessLog> logs = accessLogRepository.findByTimestampBetween(startTime, endTime);

        Map<LocalDate, Long> accessCount = logs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getTimestamp().atZone(ZoneId.of("+07:00")).toLocalDate(),
                        Collectors.counting()
                ));

        return accessCount;
    }
}
