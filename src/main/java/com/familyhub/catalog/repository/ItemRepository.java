package com.familyhub.catalog.repository;

import com.familyhub.catalog.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.category LEFT JOIN FETCH i.defaultUnit " +
           "WHERE (i.familyId IS NULL OR i.familyId = :familyId) " +
           "AND LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) AND i.active = true")
    List<Item> searchItems(UUID familyId, String query);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.category LEFT JOIN FETCH i.defaultUnit " +
           "WHERE (i.familyId IS NULL OR i.familyId = :familyId) AND i.active = true")
    List<Item> findAllByFamilyIdOrGlobal(UUID familyId);

    boolean existsByNameIgnoreCaseAndFamilyId(String name, UUID familyId);
}
