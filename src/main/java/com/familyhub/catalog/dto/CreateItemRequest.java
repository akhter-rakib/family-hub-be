package com.familyhub.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateItemRequest {
    @NotBlank
    private String name;
    private UUID categoryId;
    private UUID defaultUnitId;
}
