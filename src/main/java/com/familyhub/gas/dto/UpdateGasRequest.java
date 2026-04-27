package com.familyhub.gas.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateGasRequest {
    private LocalDate startDate;
    private String note;
}
