package com.familyhub.family.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyDto {
    private UUID id;
    private String name;
    private String description;
    private String inviteCode;
    private String myRole;
    private int memberCount;
    private LocalDateTime createdAt;
}
