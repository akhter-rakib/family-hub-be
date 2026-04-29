package com.familyhub.desco.repository;

import com.familyhub.desco.entity.DescoBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DescoBalanceRepository extends JpaRepository<DescoBalance, UUID> {

    Optional<DescoBalance> findTopByOrderByFetchedAtDesc();
    Optional<DescoBalance> findTopByFamilyIdOrderByFetchedAtDesc(UUID familyId);
}
