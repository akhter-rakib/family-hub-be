package com.familyhub.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportDto {
    private int year;
    private int month;
    private BigDecimal totalPurchaseCost;
    private BigDecimal totalBillsPaid;
    private BigDecimal grandTotal;
    private List<CategoryBreakdown> categoryBreakdown;
    private List<ItemBreakdown> itemBreakdown;
    private List<MemberBreakdown> memberBreakdown;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryBreakdown {
        private String category;
        private BigDecimal amount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemBreakdown {
        private String itemName;
        private BigDecimal amount;
        private BigDecimal totalQuantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberBreakdown {
        private String memberName;
        private BigDecimal amount;
    }
}
