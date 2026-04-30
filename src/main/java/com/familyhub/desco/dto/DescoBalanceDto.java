package com.familyhub.desco.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescoBalanceDto {
    private UUID id;
    private String accountNo;
    private String meterNo;
    private BigDecimal balance;
    private BigDecimal currentMonthConsumption;
    private LocalDateTime readingTime;
    private LocalDateTime fetchedAt;
    private boolean lowBalance;
}
