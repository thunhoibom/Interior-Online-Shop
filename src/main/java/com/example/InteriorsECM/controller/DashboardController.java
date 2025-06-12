package com.example.InteriorsECM.controller;
import com.example.InteriorsECM.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/access")
public class DashboardController {

    @Autowired
    private AccessLogService accessLogService;

    @GetMapping("/logs")
    public ResponseEntity<Map<String, Long>> getAccessLogs(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String startTimeStr,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String endTimeStr) {
        ZoneId zoneId = ZoneId.of("+07:00");
        ZonedDateTime startZoned = ZonedDateTime.parse(startTimeStr, DateTimeFormatter.ISO_DATE_TIME.withZone(zoneId));
        ZonedDateTime endZoned = ZonedDateTime.parse(endTimeStr, DateTimeFormatter.ISO_DATE_TIME.withZone(zoneId));
        Instant startInstant = startZoned.toInstant();
        Instant endInstant = endZoned.toInstant();
        System.out.println("Controller Start Time: " + startZoned + ", Controller End Time: " + endZoned);
        System.out.println("Converted Start Instant: " + startInstant + ", Converted End Instant: " + endInstant);
        Map<LocalDate, Long> accessCount = accessLogService.getAccessCountByDate(startInstant, endInstant);
        Map<String, Long> response = accessCount.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        Map.Entry::getValue
                ));
        return ResponseEntity.ok(response);
    }
}
