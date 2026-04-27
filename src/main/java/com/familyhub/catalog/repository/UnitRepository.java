package com.familyhub.catalog.repository;

import com.familyhub.catalog.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {
    Optional<Unit> findByAbbreviation(String abbreviation);
}
