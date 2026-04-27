package com.familyhub.shopping.dto;

import com.familyhub.shopping.entity.ShoppingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UpdateShoppingRequest {
    private ShoppingStatus status;
    private UUID assignedTo;
    private BigDecimal quantity;
    private UUID unitId;
    private String note;
}
