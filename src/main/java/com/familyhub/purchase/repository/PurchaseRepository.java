package com.familyhub.purchase.repository;

import com.familyhub.purchase.entity.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<PurchaseRecord, UUID> {

    @Query("SELECT pr FROM PurchaseRecord pr JOIN FETCH pr.item i LEFT JOIN FETCH i.category " +
           "JOIN FETCH pr.unit JOIN FETCH pr.purchasedBy " +
           "WHERE pr.familyId = :familyId ORDER BY pr.purchaseDate DESC, pr.createdAt DESC")
    List<PurchaseRecord> findByFamilyId(UUID familyId);

    @Query("SELECT pr FROM PurchaseRecord pr JOIN FETCH pr.item i LEFT JOIN FETCH i.category " +
           "JOIN FETCH pr.unit JOIN FETCH pr.purchasedBy " +
           "WHERE pr.familyId = :familyId AND pr.purchaseDate BETWEEN :start AND :end " +
           "ORDER BY pr.purchaseDate DESC")
    List<PurchaseRecord> findByFamilyIdAndDateRange(UUID familyId, LocalDate start, LocalDate end);

    @Query("SELECT COALESCE(SUM(pr.cost), 0) FROM PurchaseRecord pr " +
           "WHERE pr.familyId = :familyId AND pr.purchaseDate BETWEEN :start AND :end")
    BigDecimal sumCostByFamilyIdAndDateRange(UUID familyId, LocalDate start, LocalDate end);

    @Query("SELECT pr.item.category.name, COALESCE(SUM(pr.cost), 0) FROM PurchaseRecord pr " +
           "WHERE pr.familyId = :familyId AND pr.purchaseDate BETWEEN :start AND :end " +
           "GROUP BY pr.item.category.name")
    List<Object[]> sumCostByCategoryAndDateRange(UUID familyId, LocalDate start, LocalDate end);

    @Query("SELECT pr.item.name, COALESCE(SUM(pr.cost), 0), COALESCE(SUM(pr.normalizedQuantity), 0) " +
           "FROM PurchaseRecord pr WHERE pr.familyId = :familyId AND pr.purchaseDate BETWEEN :start AND :end " +
           "GROUP BY pr.item.name ORDER BY SUM(pr.cost) DESC")
    List<Object[]> sumCostByItemAndDateRange(UUID familyId, LocalDate start, LocalDate end);

    @Query("SELECT pr.purchasedBy.firstName, COALESCE(SUM(pr.cost), 0) FROM PurchaseRecord pr " +
           "WHERE pr.familyId = :familyId AND pr.purchaseDate BETWEEN :start AND :end " +
           "GROUP BY pr.purchasedBy.firstName")
    List<Object[]> sumCostByMemberAndDateRange(UUID familyId, LocalDate start, LocalDate end);
}
