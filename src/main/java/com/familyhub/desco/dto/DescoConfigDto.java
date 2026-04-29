package com.familyhub.desco.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescoConfigDto {
    private UUID id;
    private UUID familyId;
    private String accountNo;
    private String meterNo;
    private boolean enabled;
}
