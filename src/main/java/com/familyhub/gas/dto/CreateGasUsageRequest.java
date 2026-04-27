package com.familyhub.gas.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateGasUsageRequest {
    private LocalDate startDate; // defaults to today if null
    private UUID purchaseId;
    private String note;
}
