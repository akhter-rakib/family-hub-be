package com.familyhub.report.controller;

import com.familyhub.common.dto.ApiResponse;
import com.familyhub.report.dto.*;
import com.familyhub.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/families/{familyId}")
@RequiredArgsConstructor
@Tag(name = "Reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/reports/monthly")
    @Operation(summary = "Get monthly report")
    public ResponseEntity<ApiResponse<MonthlyReportDto>> getMonthlyReport(
            @PathVariable UUID familyId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getMonthlyReport(familyId, year, month)));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get dashboard summary")
    public ResponseEntity<ApiResponse<DashboardDto>> getDashboard(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getDashboard(familyId)));
    }
}
