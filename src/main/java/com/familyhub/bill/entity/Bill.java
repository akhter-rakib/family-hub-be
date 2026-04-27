package com.familyhub.bill.entity;

import com.familyhub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill extends BaseEntity {

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "bill_type", nullable = false)
    private String billType; // RENT, ELECTRICITY, WATER, SCHOOL, GAS, INTERNET, OTHER

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "paid")
    @Builder.Default
    private boolean paid = false;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "recurring")
    @Builder.Default
    private boolean recurring = false;

    @Column(name = "recurrence_interval")
    private String recurrenceInterval; // MONTHLY, QUARTERLY, YEARLY

    @Column(name = "note")
    private String note;
}
