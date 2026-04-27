package com.familyhub.shopping.dto;

import com.familyhub.shopping.entity.ShoppingStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateShoppingRequest {
    private ShoppingStatus status;
    private UUID assignedTo;
}
