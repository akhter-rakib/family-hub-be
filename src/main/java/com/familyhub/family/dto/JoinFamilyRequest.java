package com.familyhub.family.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinFamilyRequest {
    @NotBlank
    private String inviteCode;
    private String message;
}
