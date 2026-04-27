package com.familyhub.shopping.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateShoppingRequest {
    private UUID itemId;
    private String itemName; // for auto-create

    @NotNull @Positive
    private BigDecimal quantity;

    @NotNull
    private UUID unitId;

    private UUID assignedTo;
    private Integer priority;
    private LocalDate dueDate;
    private String note;
    private UUID categoryId; // for auto-create item
}
