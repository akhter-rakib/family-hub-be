package com.familyhub.desco.entity;

import com.familyhub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "desco_config", uniqueConstraints = @UniqueConstraint(columnNames = "family_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescoConfig extends BaseEntity {
    @Column(name = "family_id", nullable = false, length = 36)
    private UUID familyId;

    @Column(name = "account_no", nullable = false, length = 20)
    private String accountNo;

    @Column(name = "meter_no", nullable = false, length = 20)
    private String meterNo;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private boolean enabled = true;
}
