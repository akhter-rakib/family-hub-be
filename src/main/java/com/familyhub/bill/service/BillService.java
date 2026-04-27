package com.familyhub.bill.service;

import com.familyhub.bill.dto.*;
import com.familyhub.bill.entity.Bill;
import com.familyhub.bill.repository.BillRepository;
import com.familyhub.common.event.AppEvent;
import com.familyhub.common.event.EventPublisher;
import com.familyhub.common.exception.ResourceNotFoundException;
import com.familyhub.common.security.SecurityUtils;
import com.familyhub.family.service.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final FamilyService familyService;
    private final EventPublisher eventPublisher;

    @Transactional
    public BillDto createBill(UUID familyId, CreateBillRequest request) {
        familyService.validateMembership(familyId);

        Bill bill = Bill.builder()
                .familyId(familyId)
                .title(request.getTitle())
                .billType(request.getBillType())
                .amount(request.getAmount())
                .dueDate(request.getDueDate())
                .recurring(request.isRecurring())
                .recurrenceInterval(request.getRecurrenceInterval())
                .note(request.getNote())
                .build();

        bill = billRepository.save(bill);
        log.info("Bill created: {} for family: {}", bill.getTitle(), familyId);
        return toDto(bill);
    }

    public List<BillDto> getBills(UUID familyId) {
        familyService.validateMembership(familyId);
        return billRepository.findByFamilyIdOrderByDueDateDesc(familyId).stream()
                .map(this::toDto).toList();
    }

    public List<BillDto> getOverdueBills(UUID familyId) {
        familyService.validateMembership(familyId);
        return billRepository.findByFamilyIdAndPaidFalseAndDueDateBeforeOrderByDueDateAsc(familyId, LocalDate.now())
                .stream().map(this::toDto).toList();
    }

    @Transactional
    public BillDto markPaid(UUID familyId, UUID billId) {
        familyService.validateMembership(familyId);

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", billId));

        bill.setPaid(true);
        bill.setPaidDate(LocalDate.now());
        bill = billRepository.save(bill);

        eventPublisher.publishReportUpdate(
                AppEvent.of("bill_paid", familyId, SecurityUtils.getCurrentUserId(),
                        bill.getTitle() + " bill paid"));

        return toDto(bill);
    }

    private BillDto toDto(Bill bill) {
        return BillDto.builder()
                .id(bill.getId())
                .familyId(bill.getFamilyId())
                .title(bill.getTitle())
                .billType(bill.getBillType())
                .amount(bill.getAmount())
                .dueDate(bill.getDueDate())
                .paid(bill.isPaid())
                .paidDate(bill.getPaidDate())
                .recurring(bill.isRecurring())
                .recurrenceInterval(bill.getRecurrenceInterval())
                .note(bill.getNote())
                .createdAt(bill.getCreatedAt())
                .build();
    }
}
