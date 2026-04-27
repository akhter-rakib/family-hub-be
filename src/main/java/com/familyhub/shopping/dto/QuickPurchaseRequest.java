package com.familyhub.shopping.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class QuickPurchaseRequest {
    @NotNull @Positive
    private BigDecimal actualQuantity;

    @NotNull @Positive
    private BigDecimal cost;

    private String shopName;
    private String note;
}
