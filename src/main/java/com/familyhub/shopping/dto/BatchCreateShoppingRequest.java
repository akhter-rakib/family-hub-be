package com.familyhub.shopping.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BatchCreateShoppingRequest {
    @NotEmpty
    @Valid
    private List<CreateShoppingRequest> items;

    private UUID assignedTo; // applies to all items if set

    private String listName; // optional group name, e.g. "Eid Shop"
}
