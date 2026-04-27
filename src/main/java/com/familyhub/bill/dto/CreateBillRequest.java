package com.familyhub.bill.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateBillRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String billType;

    @NotNull @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDate dueDate;

    private boolean recurring;
    private String recurrenceInterval;
    private String note;
}
