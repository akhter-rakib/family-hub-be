package com.familyhub.inventory.service;

import com.familyhub.catalog.entity.Item;
import com.familyhub.catalog.entity.Unit;
import com.familyhub.catalog.repository.ItemRepository;
import com.familyhub.catalog.service.CatalogService;
import com.familyhub.common.exception.ResourceNotFoundException;
import com.familyhub.family.service.FamilyService;
import com.familyhub.inventory.dto.*;
import com.familyhub.inventory.entity.InventoryItem;
import com.familyhub.inventory.repository.InventoryItemRepository;
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
public class InventoryService {

    private final InventoryItemRepository inventoryRepo;
    private final ItemRepository itemRepository;
    private final CatalogService catalogService;
    private final FamilyService familyService;

    @Transactional
    public InventoryItemDto upsertInventory(UUID familyId, UpsertInventoryRequest request) {
        familyService.validateMembership(familyId);

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", request.getItemId()));
        Unit unit = catalogService.getUnitEntity(request.getUnitId());

        InventoryItem inv = inventoryRepo.findByFamilyIdAndItemId(familyId, request.getItemId())
                .orElse(InventoryItem.builder()
                        .familyId(familyId)
                        .item(item)
                        .unit(unit)
                        .build());

        inv.setQuantity(request.getQuantity());
        inv.setUnit(unit);
        if (request.getLowStockThreshold() != null) inv.setLowStockThreshold(request.getLowStockThreshold());
        if (request.getExpiryDate() != null) inv.setExpiryDate(request.getExpiryDate());

        inv = inventoryRepo.save(inv);
        return toDto(inv);
    }

    public List<InventoryItemDto> getInventory(UUID familyId) {
        familyService.validateMembership(familyId);
        return inventoryRepo.findByFamilyId(familyId).stream().map(this::toDto).toList();
    }

    public List<InventoryItemDto> getLowStock(UUID familyId) {
        familyService.validateMembership(familyId);
        return inventoryRepo.findLowStock(familyId).stream().map(this::toDto).toList();
    }

    public List<InventoryItemDto> getExpiringSoon(UUID familyId) {
        familyService.validateMembership(familyId);
        return inventoryRepo.findExpiringSoon(familyId, LocalDate.now().plusDays(7))
                .stream().map(this::toDto).toList();
    }

    private InventoryItemDto toDto(InventoryItem inv) {
        boolean isLowStock = inv.getLowStockThreshold() != null &&
                inv.getQuantity().compareTo(inv.getLowStockThreshold()) <= 0;
        boolean isExpiring = inv.getExpiryDate() != null &&
                !inv.getExpiryDate().isAfter(LocalDate.now().plusDays(7));

        return InventoryItemDto.builder()
                .id(inv.getId())
                .familyId(inv.getFamilyId())
                .itemId(inv.getItem().getId())
                .itemName(inv.getItem().getName())
                .quantity(inv.getQuantity())
                .unitAbbreviation(inv.getUnit().getAbbreviation())
                .lowStockThreshold(inv.getLowStockThreshold())
                .expiryDate(inv.getExpiryDate())
                .lowStock(isLowStock)
                .expiringSoon(isExpiring)
                .build();
    }
}
