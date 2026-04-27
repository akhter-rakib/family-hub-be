package com.familyhub.shopping.dto;

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
public class ShoppingRequestDto {
    private UUID id;
    private UUID familyId;
    private UUID itemId;
    private String itemName;
    private String categoryName;
    private BigDecimal quantity;
    private UUID unitId;
    private String unitAbbreviation;
    private BigDecimal normalizedQuantity;
    private String status;
    private int priority;
    private LocalDate dueDate;
    private String note;
    private UUID requestedById;
    private String requestedByName;
    private UUID assignedToId;
    private String assignedToName;
    private String listName;
    private LocalDateTime createdAt;
}
