package com.familyhub.bill.repository;

import com.familyhub.bill.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID> {

    List<Bill> findByFamilyIdOrderByDueDateDesc(UUID familyId);

    List<Bill> findByFamilyIdAndPaidFalseAndDueDateBeforeOrderByDueDateAsc(UUID familyId, LocalDate date);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Bill b WHERE b.familyId = :familyId AND b.paid = true " +
           "AND b.paidDate BETWEEN :start AND :end")
    BigDecimal sumPaidBillsByDateRange(UUID familyId, LocalDate start, LocalDate end);
}
