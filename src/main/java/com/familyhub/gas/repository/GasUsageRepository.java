package com.familyhub.gas.repository;

import com.familyhub.gas.entity.GasUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GasUsageRepository extends JpaRepository<GasUsageLog, UUID> {
    @Query("SELECT g FROM GasUsageLog g LEFT JOIN FETCH g.purchase p LEFT JOIN FETCH p.item " +
           "WHERE g.familyId = :familyId ORDER BY g.startDate DESC")
    List<GasUsageLog> findByFamilyIdOrderByStartDateDesc(UUID familyId);

    Optional<GasUsageLog> findByFamilyIdAndStatus(UUID familyId, String status);

    @Query("SELECT COALESCE(SUM(g.cost), 0) FROM GasUsageLog g " +
           "WHERE g.familyId = :familyId AND g.startDate BETWEEN :start AND :end AND g.cost IS NOT NULL")
    BigDecimal sumCostByFamilyIdAndDateRange(UUID familyId, LocalDate start, LocalDate end);
}
