package com.familyhub.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDto {
    private UUID id;
    private UUID familyId;
    private UUID itemId;
    private String itemName;
    private BigDecimal quantity;
    private String unitAbbreviation;
    private BigDecimal lowStockThreshold;
    private LocalDate expiryDate;
    private boolean lowStock;
    private boolean expiringSoon;
}
