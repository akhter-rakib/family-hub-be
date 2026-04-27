package com.familyhub.family.entity;

import com.familyhub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "families")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Family extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "invite_code", unique = true, nullable = false, length = 10)
    private String inviteCode;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;
}
