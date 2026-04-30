package com.familyhub.desco.entity;

import com.familyhub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "desco_balance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescoBalance extends BaseEntity {

    @Column(name = "account_no", nullable = false, length = 20)
    private String accountNo;

    @Column(name = "meter_no", nullable = false, length = 20)
    private String meterNo;

    @Column(name = "balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal balance;

    @Column(name = "current_month_consumption", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentMonthConsumption;

    @Column(name = "reading_time", nullable = false)
    private LocalDateTime readingTime;

    @Column(name = "fetched_at", nullable = false)
    private LocalDateTime fetchedAt;

    @Column(name = "family_id", nullable = false, length = 36)
    private UUID familyId;
}
