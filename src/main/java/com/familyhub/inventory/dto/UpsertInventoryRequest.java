package com.familyhub.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpsertInventoryRequest {
    @NotNull
    private UUID itemId;

    @NotNull @Positive
    private BigDecimal quantity;

    @NotNull
    private UUID unitId;

    private BigDecimal lowStockThreshold;
    private LocalDate expiryDate;
}
