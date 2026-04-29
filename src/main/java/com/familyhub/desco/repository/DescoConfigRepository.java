package com.familyhub.desco.repository;

import com.familyhub.desco.entity.DescoConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DescoConfigRepository extends JpaRepository<DescoConfig, UUID> {
    Optional<DescoConfig> findByFamilyId(UUID familyId);
    boolean existsByFamilyId(UUID familyId);
    List<DescoConfig> findAllByEnabledTrue();
}
