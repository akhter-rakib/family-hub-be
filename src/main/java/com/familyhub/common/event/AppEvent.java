package com.familyhub.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppEvent implements Serializable {
    private String eventId;
    private String type;
    private UUID familyId;
    private UUID userId;
    private String message;
    private Object payload;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static AppEvent of(String type, UUID familyId, UUID userId, String message) {
        return AppEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .type(type)
                .familyId(familyId)
                .userId(userId)
                .message(message)
                .build();
    }
}
