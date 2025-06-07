package com.example.InteriorsECM.service;

public interface AccessLogService {
    void logAccessEvent(Long userId, String eventType, String ipAddress, boolean success);
}
