package com.familyhub.gas.dto;

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
public class AvailableCylinderDto {
    private UUID purchaseId;
    private String itemName;
    private BigDecimal cost;
    private LocalDate purchaseDate;
    private String shopName;
    private String purchasedByName;
    private String note;
}
