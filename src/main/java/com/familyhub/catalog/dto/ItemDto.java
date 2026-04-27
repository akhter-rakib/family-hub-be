package com.familyhub.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private UUID id;
    private String name;
    private UUID categoryId;
    private String categoryName;
    private UUID defaultUnitId;
    private String defaultUnitName;
    private boolean global;
}
