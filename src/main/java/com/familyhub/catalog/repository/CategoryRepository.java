package com.familyhub.catalog.repository;

import com.familyhub.catalog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c WHERE c.familyId IS NULL OR c.familyId = :familyId")
    List<Category> findAllByFamilyIdOrGlobal(UUID familyId);
}
