package com.familyhub.purchase.service;

import com.familyhub.auth.entity.User;
import com.familyhub.auth.repository.UserRepository;
import com.familyhub.catalog.dto.ItemDto;
import com.familyhub.catalog.entity.Item;
import com.familyhub.catalog.entity.Unit;
import com.familyhub.catalog.repository.ItemRepository;
import com.familyhub.catalog.service.CatalogService;
import com.familyhub.common.event.AppEvent;
import com.familyhub.common.event.EventPublisher;
import com.familyhub.common.exception.BadRequestException;
import com.familyhub.common.exception.ResourceNotFoundException;
import com.familyhub.common.security.SecurityUtils;
import com.familyhub.family.service.FamilyService;
import com.familyhub.purchase.dto.*;
import com.familyhub.purchase.entity.PurchaseRecord;
import com.familyhub.purchase.repository.PurchaseRepository;
import com.familyhub.shopping.entity.ShoppingRequest;
import com.familyhub.shopping.entity.ShoppingStatus;
import com.familyhub.shopping.repository.ShoppingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ShoppingRequestRepository shoppingRepo;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CatalogService catalogService;
    private final FamilyService familyService;
    private final EventPublisher eventPublisher;

    @Transactional
    @CacheEvict(value = "dashboard", key = "#familyId")
    public PurchaseDto createPurchase(UUID familyId, CreatePurchaseRequest request) {
        familyService.validateMembership(familyId);
        UUID userId = SecurityUtils.getCurrentUserId();

        // Resolve item
        Item item;
        if (request.getItemId() != null) {
            item = itemRepository.findById(request.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item", "id", request.getItemId()));
        } else if (request.getItemName() != null && !request.getItemName().isBlank()) {
            ItemDto created = catalogService.getOrCreateItem(familyId, request.getItemName(),
                    request.getCategoryId(), request.getUnitId());
            item = itemRepository.findById(created.getId()).orElseThrow();
        } else {
            throw new BadRequestException("Either itemId or itemName is required");
        }

        Unit unit = catalogService.getUnitEntity(request.getUnitId());
        BigDecimal normalizedQty = request.getQuantity();
        if (unit.getConversionFactor() != null) {
            normalizedQty = request.getQuantity().multiply(unit.getConversionFactor());
        }

        User purchaser = userRepository.findById(userId).orElseThrow();

        PurchaseRecord record = PurchaseRecord.builder()
                .familyId(familyId)
                .item(item)
                .quantity(request.getQuantity())
                .unit(unit)
                .normalizedQuantity(normalizedQty)
                .cost(request.getCost())
                .shopName(request.getShopName())
                .purchaseDate(request.getPurchaseDate() != null ? request.getPurchaseDate() : LocalDate.now())
                .receiptUrl(request.getReceiptUrl())
                .note(request.getNote())
                .purchasedBy(purchaser)
                .build();

        // Link to shopping request and update status
        if (request.getShoppingRequestId() != null) {
            ShoppingRequest sr = shoppingRepo.findById(request.getShoppingRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("ShoppingRequest", "id", request.getShoppingRequestId()));
            record.setShoppingRequest(sr);
            sr.setStatus(ShoppingStatus.BOUGHT);
            shoppingRepo.save(sr);
        }

        record = purchaseRepository.save(record);
        log.info("Purchase recorded: {} for family: {}", item.getName(), familyId);

        // Publish events
        eventPublisher.publishInventoryUpdate(
                AppEvent.of("item_purchased", familyId, userId, item.getName() + " purchased"));
        eventPublisher.publishReportUpdate(
                AppEvent.of("purchase_recorded", familyId, userId, "New purchase: " + item.getName()));

        return toDto(record);
    }

    public List<PurchaseDto> getPurchases(UUID familyId) {
        familyService.validateMembership(familyId);
        return purchaseRepository.findByFamilyId(familyId).stream().map(this::toDto).toList();
    }

    public List<PurchaseDto> getPurchasesByDateRange(UUID familyId, LocalDate start, LocalDate end) {
        familyService.validateMembership(familyId);
        return purchaseRepository.findByFamilyIdAndDateRange(familyId, start, end).stream()
                .map(this::toDto).toList();
    }

    private PurchaseDto toDto(PurchaseRecord pr) {
        return PurchaseDto.builder()
                .id(pr.getId())
                .familyId(pr.getFamilyId())
                .itemId(pr.getItem().getId())
                .itemName(pr.getItem().getName())
                .categoryName(pr.getItem().getCategory() != null ? pr.getItem().getCategory().getName() : null)
                .quantity(pr.getQuantity())
                .unitAbbreviation(pr.getUnit().getAbbreviation())
                .normalizedQuantity(pr.getNormalizedQuantity())
                .cost(pr.getCost())
                .shopName(pr.getShopName())
                .purchaseDate(pr.getPurchaseDate())
                .receiptUrl(pr.getReceiptUrl())
                .note(pr.getNote())
                .purchasedById(pr.getPurchasedBy().getId())
                .purchasedByName(pr.getPurchasedBy().getFullName())
                .shoppingRequestId(pr.getShoppingRequest() != null ? pr.getShoppingRequest().getId() : null)
                .createdAt(pr.getCreatedAt())
                .build();
    }
}
