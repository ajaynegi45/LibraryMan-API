package com.libraryman_api.analytics;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getLibraryOverview() {
        return ResponseEntity.ok(analyticsService.getLibraryOverview());
    }

    @GetMapping("/popular-books")
    public ResponseEntity<List<Map<String, Object>>> getPopularBooks(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getPopularBooks(limit));
    }

    @GetMapping("/borrowing-trends")
    public ResponseEntity<Map<String, Long>> getBorrowingTrends(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(analyticsService.getBorrowingTrends(startDate, endDate));
    }

    @GetMapping("/member-activity")
    public ResponseEntity<List<Map<String, Object>>> getMemberActivityReport() {
        return ResponseEntity.ok(analyticsService.getMemberActivityReport());
    }
}