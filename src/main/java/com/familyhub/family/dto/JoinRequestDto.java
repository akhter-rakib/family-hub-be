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
public class JoinRequestDto {
    private UUID id;
    private UUID userId;
    private String userName;
    private String userEmail;
    private String status;
    private String message;
    private LocalDateTime createdAt;
}
