package com.familyhub.catalog.entity;

import com.familyhub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_unit_id")
    private Unit defaultUnit;

    @Column(name = "family_id")
    private UUID familyId; // null = global

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;
}
