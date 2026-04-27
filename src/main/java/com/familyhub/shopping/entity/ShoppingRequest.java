package com.familyhub.shopping.entity;

import com.familyhub.auth.entity.User;
import com.familyhub.catalog.entity.Item;
import com.familyhub.catalog.entity.Unit;
import com.familyhub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "shopping_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingRequest extends BaseEntity {

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Column(name = "normalized_quantity", precision = 15, scale = 6)
    private BigDecimal normalizedQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ShoppingStatus status = ShoppingStatus.PENDING;

    @Column(name = "priority")
    @Builder.Default
    private Integer priority = 0; // 0=normal, 1=high, 2=urgent

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "note")
    private String note;

    @Column(name = "list_name")
    private String listName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;
}
