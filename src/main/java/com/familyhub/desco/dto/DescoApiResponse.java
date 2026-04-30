package com.familyhub.desco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DescoApiResponse {
    private int code;
    private String desc;
    private DescoData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DescoData {
        private String accountNo;
        private String meterNo;
        private BigDecimal balance;
        private BigDecimal currentMonthConsumption;
        private String readingTime;
    }
}
