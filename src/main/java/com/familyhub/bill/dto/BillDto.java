package com.familyhub.bill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDto {
    private UUID id;
    private UUID familyId;
    private String title;
    private String billType;
    private BigDecimal amount;
    private LocalDate dueDate;
    private boolean paid;
    private LocalDate paidDate;
    private boolean recurring;
    private String recurrenceInterval;
    private String note;
    private LocalDateTime createdAt;
}
