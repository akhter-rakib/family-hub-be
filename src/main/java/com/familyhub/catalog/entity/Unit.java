package com.familyhub.catalog.entity;

import com.familyhub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "abbreviation", nullable = false, unique = true, length = 10)
    private String abbreviation;

    @Column(name = "base_unit", length = 10)
    private String baseUnit;

    @Column(name = "conversion_factor", precision = 15, scale = 6)
    private BigDecimal conversionFactor;

    @Column(name = "unit_type", nullable = false, length = 20)
    private String unitType; // WEIGHT, VOLUME, COUNT
}
