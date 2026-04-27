package com.familyhub.family.dto;

import com.familyhub.family.entity.FamilyRole;
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
public class FamilyMemberDto {
    private UUID id;
    private UUID userId;
    private String name;
    private String email;
    private FamilyRole role;
    private LocalDateTime joinedAt;
}
