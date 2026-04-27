package com.familyhub.purchase.dto;

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
public class PurchaseDto {
    private UUID id;
    private UUID familyId;
    private UUID itemId;
    private String itemName;
    private String categoryName;
    private BigDecimal quantity;
    private String unitAbbreviation;
    private BigDecimal normalizedQuantity;
    private BigDecimal cost;
    private String shopName;
    private LocalDate purchaseDate;
    private String receiptUrl;
    private String note;
    private UUID purchasedById;
    private String purchasedByName;
    private UUID shoppingRequestId;
    private LocalDateTime createdAt;
}
