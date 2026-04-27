package com.familyhub.gas.entity;

import com.familyhub.common.entity.BaseEntity;
import com.familyhub.purchase.entity.PurchaseRecord;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "gas_usage_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GasUsageLog extends BaseEntity {

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "days_used")
    private Integer daysUsed;

    @Column(name = "cost", precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, FINISHED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private PurchaseRecord purchase;

    @Column(name = "note")
    private String note;
}
