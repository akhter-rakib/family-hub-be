package com.familyhub.family.repository;

import com.familyhub.family.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FamilyRepository extends JpaRepository<Family, UUID> {
    Optional<Family> findByInviteCode(String inviteCode);
}
