package com.familyhub.inventory.repository;

import com.familyhub.inventory.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {

    @Query("SELECT ii FROM InventoryItem ii JOIN FETCH ii.item JOIN FETCH ii.unit " +
           "WHERE ii.familyId = :familyId ORDER BY ii.item.name")
    List<InventoryItem> findByFamilyId(UUID familyId);

    Optional<InventoryItem> findByFamilyIdAndItemId(UUID familyId, UUID itemId);

    @Query("SELECT ii FROM InventoryItem ii JOIN FETCH ii.item JOIN FETCH ii.unit " +
           "WHERE ii.familyId = :familyId AND ii.quantity <= ii.lowStockThreshold")
    List<InventoryItem> findLowStock(UUID familyId);

    @Query("SELECT ii FROM InventoryItem ii JOIN FETCH ii.item JOIN FETCH ii.unit " +
           "WHERE ii.familyId = :familyId AND ii.expiryDate IS NOT NULL AND ii.expiryDate <= :date")
    List<InventoryItem> findExpiringSoon(UUID familyId, LocalDate date);
}
