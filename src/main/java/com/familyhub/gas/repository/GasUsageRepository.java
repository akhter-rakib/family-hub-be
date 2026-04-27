package com.familyhub.gas.repository;

import com.familyhub.gas.entity.GasUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GasUsageRepository extends JpaRepository<GasUsageLog, UUID> {
    List<GasUsageLog> findByFamilyIdOrderByStartDateDesc(UUID familyId);
    Optional<GasUsageLog> findByFamilyIdAndStatus(UUID familyId, String status);
}
