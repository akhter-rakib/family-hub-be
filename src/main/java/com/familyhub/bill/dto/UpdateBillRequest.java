package com.familyhub.bill.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateBillRequest {
    private String title;
    private String billType;

    @Positive
    private BigDecimal amount;

    private LocalDate dueDate;
    private Boolean recurring;
    private String recurrenceInterval;
    private String note;
}
