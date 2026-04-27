package com.familyhub.notification.entity;

import com.familyhub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "family_id")
    private UUID familyId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Column(name = "read_status", nullable = false)
    @Builder.Default
    private boolean readStatus = false;
}
