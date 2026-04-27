package com.familyhub.purchase.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreatePurchaseRequest {
    private UUID shoppingRequestId;
    private UUID itemId;
    private String itemName;

    @NotNull @Positive
    private BigDecimal quantity;

    @NotNull
    private UUID unitId;

    @NotNull @Positive
    private BigDecimal cost;

    private String shopName;
    private LocalDate purchaseDate;
    private String receiptUrl;
    private String note;
    private UUID categoryId;
}
