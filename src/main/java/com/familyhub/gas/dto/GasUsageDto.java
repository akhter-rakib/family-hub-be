package com.familyhub.gas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GasUsageDto {
    private UUID id;
    private UUID familyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer daysUsed;
    private BigDecimal cost;
    private String status;
    private UUID purchaseId;
    private String purchaseItemName;
    private String purchaseShopName;
    private String note;
    private LocalDateTime createdAt;
}
