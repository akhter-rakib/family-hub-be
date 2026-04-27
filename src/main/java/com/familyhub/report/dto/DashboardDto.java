package com.familyhub.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private int totalMembers;
    private int pendingShoppingRequests;
    private int overdueBills;
    private BigDecimal monthlySpending;
    private int lowStockItems;
    private int unreadNotifications;
}
