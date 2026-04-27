package com.familyhub.gas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateGasUsageRequest {
    @NotNull
    private LocalDate startDate;
    private BigDecimal cost;
    private UUID purchaseId;
    private String note;
}
