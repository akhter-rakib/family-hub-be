package com.familyhub.family.entity;

import com.familyhub.auth.entity.User;
import com.familyhub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "family_members", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"family_id", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private FamilyRole role;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;
}
