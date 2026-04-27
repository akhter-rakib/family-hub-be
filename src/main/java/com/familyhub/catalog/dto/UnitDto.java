package com.familyhub.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitDto {
    private UUID id;
    private String name;
    private String abbreviation;
    private String baseUnit;
    private BigDecimal conversionFactor;
    private String unitType;
}
