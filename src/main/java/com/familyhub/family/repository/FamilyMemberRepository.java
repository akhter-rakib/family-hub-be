package com.familyhub.family.repository;

import com.familyhub.family.entity.FamilyMember;
import com.familyhub.family.entity.FamilyRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, UUID> {

    Optional<FamilyMember> findByFamilyIdAndUserId(UUID familyId, UUID userId);

    boolean existsByFamilyIdAndUserIdAndActiveTrue(UUID familyId, UUID userId);

    @Query("SELECT fm FROM FamilyMember fm JOIN FETCH fm.family WHERE fm.user.id = :userId AND fm.active = true")
    List<FamilyMember> findActiveMembershipsByUserId(UUID userId);

    @Query("SELECT fm FROM FamilyMember fm JOIN FETCH fm.user WHERE fm.family.id = :familyId AND fm.active = true")
    List<FamilyMember> findActiveMembersByFamilyId(UUID familyId);

    Optional<FamilyMember> findByFamilyIdAndUserIdAndActiveTrue(UUID familyId, UUID userId);

    @Query("SELECT fm.role FROM FamilyMember fm WHERE fm.family.id = :familyId AND fm.user.id = :userId AND fm.active = true")
    Optional<FamilyRole> findRoleByFamilyIdAndUserId(UUID familyId, UUID userId);
}
