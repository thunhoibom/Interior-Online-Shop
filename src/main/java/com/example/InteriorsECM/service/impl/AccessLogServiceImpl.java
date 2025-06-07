package com.example.InteriorsECM.service.impl;

import com.example.InteriorsECM.model.timescaledb.AccessLog;
import com.example.InteriorsECM.repository.timescaledb.AccessLogRepository;
import com.example.InteriorsECM.service.AccessLogService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
            System.out.println("Log saved successfully");
        } catch (Exception e) {
            System.err.println("Error saving log: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
