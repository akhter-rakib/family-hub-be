package com.familyhub.shopping.repository;

import com.familyhub.shopping.entity.ShoppingRequest;
import com.familyhub.shopping.entity.ShoppingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ShoppingRequestRepository extends JpaRepository<ShoppingRequest, UUID> {

    @Query("SELECT sr FROM ShoppingRequest sr JOIN FETCH sr.item i LEFT JOIN FETCH i.category " +
           "JOIN FETCH sr.unit LEFT JOIN FETCH sr.requestedBy LEFT JOIN FETCH sr.assignedTo " +
           "WHERE sr.familyId = :familyId ORDER BY sr.priority DESC, sr.createdAt DESC")
    List<ShoppingRequest> findByFamilyId(UUID familyId);

    @Query("SELECT sr FROM ShoppingRequest sr JOIN FETCH sr.item i LEFT JOIN FETCH i.category " +
           "JOIN FETCH sr.unit LEFT JOIN FETCH sr.requestedBy LEFT JOIN FETCH sr.assignedTo " +
           "WHERE sr.familyId = :familyId AND sr.status = :status ORDER BY sr.priority DESC, sr.createdAt DESC")
    List<ShoppingRequest> findByFamilyIdAndStatus(UUID familyId, ShoppingStatus status);

    @Query("SELECT sr FROM ShoppingRequest sr JOIN FETCH sr.item i LEFT JOIN FETCH i.category " +
           "JOIN FETCH sr.unit LEFT JOIN FETCH sr.requestedBy LEFT JOIN FETCH sr.assignedTo " +
           "WHERE sr.assignedTo.id = :userId AND sr.status IN ('PENDING', 'ACCEPTED') " +
           "ORDER BY sr.priority DESC, sr.dueDate ASC")
    List<ShoppingRequest> findMyTasks(UUID userId);
}
