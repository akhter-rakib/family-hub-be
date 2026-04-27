package com.familyhub.gas.service;

import com.familyhub.common.exception.BadRequestException;
import com.familyhub.common.exception.ResourceNotFoundException;
import com.familyhub.family.service.FamilyService;
import com.familyhub.gas.dto.*;
import com.familyhub.gas.entity.GasUsageLog;
import com.familyhub.gas.repository.GasUsageRepository;
import com.familyhub.purchase.entity.PurchaseRecord;
import com.familyhub.purchase.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GasService {

    private final GasUsageRepository gasRepository;
    private final PurchaseRepository purchaseRepository;
    private final FamilyService familyService;

    @Transactional
    public GasUsageDto startGasUsage(UUID familyId, CreateGasUsageRequest request) {
        familyService.validateMembership(familyId);

        gasRepository.findByFamilyIdAndStatus(familyId, "ACTIVE")
                .ifPresent(active -> { throw new BadRequestException("An active gas usage already exists. Finish it first."); });

        PurchaseRecord purchase = null;
        if (request.getPurchaseId() != null) {
            purchase = purchaseRepository.findById(request.getPurchaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("PurchaseRecord", "id", request.getPurchaseId()));
        }

        GasUsageLog gasLog = GasUsageLog.builder()
                .familyId(familyId)
                .startDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now())
                .cost(purchase != null ? purchase.getCost() : null)
                .purchase(purchase)
                .note(request.getNote())
                .build();

        gasLog = gasRepository.save(gasLog);
        log.info("Gas usage started for family: {}", familyId);
        return toDto(gasLog);
    }

    @Transactional
    public GasUsageDto updateGasUsage(UUID familyId, UUID gasId, UpdateGasRequest request) {
        familyService.validateMembership(familyId);

        GasUsageLog gasLog = gasRepository.findById(gasId)
                .orElseThrow(() -> new ResourceNotFoundException("GasUsageLog", "id", gasId));

        if (request.getStartDate() != null) gasLog.setStartDate(request.getStartDate());
        if (request.getNote() != null) gasLog.setNote(request.getNote());

        // Recalculate daysUsed if finished and startDate changed
        if ("FINISHED".equals(gasLog.getStatus()) && gasLog.getEndDate() != null) {
            gasLog.setDaysUsed((int) ChronoUnit.DAYS.between(gasLog.getStartDate(), gasLog.getEndDate()));
        }

        gasLog = gasRepository.save(gasLog);
        log.info("Gas log updated: {} for family: {}", gasId, familyId);
        return toDto(gasLog);
    }

    @Transactional
    public GasUsageDto finishGasUsage(UUID familyId, UUID gasId) {
        familyService.validateMembership(familyId);

        GasUsageLog gasLog = gasRepository.findById(gasId)
                .orElseThrow(() -> new ResourceNotFoundException("GasUsageLog", "id", gasId));

        gasLog.setEndDate(LocalDate.now());
        gasLog.setDaysUsed((int) ChronoUnit.DAYS.between(gasLog.getStartDate(), LocalDate.now()));
        gasLog.setStatus("FINISHED");

        gasLog = gasRepository.save(gasLog);
        return toDto(gasLog);
    }

    public List<GasUsageDto> getGasUsageLogs(UUID familyId) {
        familyService.validateMembership(familyId);
        return gasRepository.findByFamilyIdOrderByStartDateDesc(familyId).stream()
                .map(this::toDto).toList();
    }

    public List<AvailableCylinderDto> getAvailableCylinders(UUID familyId) {
        familyService.validateMembership(familyId);
        return purchaseRepository.findAvailableGasCylinders(familyId).stream()
                .map(pr -> AvailableCylinderDto.builder()
                        .purchaseId(pr.getId())
                        .itemName(pr.getItem().getName())
                        .cost(pr.getCost())
                        .purchaseDate(pr.getPurchaseDate())
                        .shopName(pr.getShopName())
                        .purchasedByName(pr.getPurchasedBy().getFirstName())
                        .note(pr.getNote())
                        .build())
                .toList();
    }

    private GasUsageDto toDto(GasUsageLog g) {
        return GasUsageDto.builder()
                .id(g.getId())
                .familyId(g.getFamilyId())
                .startDate(g.getStartDate())
                .endDate(g.getEndDate())
                .daysUsed(g.getDaysUsed())
                .cost(g.getCost())
                .status(g.getStatus())
                .purchaseId(g.getPurchase() != null ? g.getPurchase().getId() : null)
                .purchaseItemName(g.getPurchase() != null ? g.getPurchase().getItem().getName() : null)
                .purchaseShopName(g.getPurchase() != null ? g.getPurchase().getShopName() : null)
                .note(g.getNote())
                .createdAt(g.getCreatedAt())
                .build();
    }
}
