package com.familyhub.shopping.service;

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
import com.familyhub.family.entity.FamilyMember;
import com.familyhub.family.repository.FamilyMemberRepository;
import com.familyhub.family.service.FamilyService;
import com.familyhub.purchase.dto.CreatePurchaseRequest;
import com.familyhub.purchase.service.PurchaseService;
import com.familyhub.shopping.dto.*;
import com.familyhub.shopping.entity.ShoppingRequest;
import com.familyhub.shopping.entity.ShoppingStatus;
import com.familyhub.shopping.repository.ShoppingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingService {

    private final ShoppingRequestRepository shoppingRepo;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CatalogService catalogService;
    private final FamilyService familyService;
    private final FamilyMemberRepository memberRepository;
    private final EventPublisher eventPublisher;
    private final PurchaseService purchaseService;

    @Transactional
    public ShoppingRequestDto createRequest(UUID familyId, CreateShoppingRequest request) {
        return createRequestInternal(familyId, request, null);
    }

    private ShoppingRequestDto createRequestInternal(UUID familyId, CreateShoppingRequest request, String listName) {
        familyService.validateMembership(familyId);
        UUID userId = SecurityUtils.getCurrentUserId();

        // Resolve or create item
        Item item = resolveItem(familyId, request);
        Unit unit = resolveDefaultUnit(item, request.getUnitId());
        BigDecimal normalizedQty = normalizeQuantity(request.getQuantity(), unit);

        User requester = userRepository.findById(userId).orElseThrow();

        // Auto-assign: if assignedTo not specified AND family has exactly 2 members, assign to the other
        UUID assigneeId = request.getAssignedTo();
        if (assigneeId == null) {
            assigneeId = resolveAutoAssignee(familyId, userId);
        }

        User assignee = null;
        if (assigneeId != null) {
            assignee = userRepository.findById(assigneeId).orElse(null);
        }

        ShoppingRequest sr = ShoppingRequest.builder()
                .familyId(familyId)
                .item(item)
                .quantity(request.getQuantity())
                .unit(unit)
                .normalizedQuantity(normalizedQty)
                .priority(request.getPriority() != null ? request.getPriority() : 0)
                .dueDate(request.getDueDate())
                .note(request.getNote())
                .listName(listName)
                .requestedBy(requester)
                .assignedTo(assignee)
                .build();

        sr = shoppingRepo.save(sr);
        log.info("Shopping request created: {} for family: {}", item.getName(), familyId);

        if (assignee != null) {
            eventPublisher.publishNotification(
                    AppEvent.of("shopping_assigned", familyId, assignee.getId(),
                            requester.getFullName() + " assigned you: " + item.getName()));
        }

        return toDto(sr);
    }

    @Transactional
    public List<ShoppingRequestDto> batchCreate(UUID familyId, BatchCreateShoppingRequest batch) {
        familyService.validateMembership(familyId);
        UUID userId = SecurityUtils.getCurrentUserId();

        // If batch-level assignee set, apply to all items
        UUID batchAssignee = batch.getAssignedTo();
        if (batchAssignee == null) {
            batchAssignee = resolveAutoAssignee(familyId, userId);
        }

        final UUID finalAssignee = batchAssignee;
        final String listName = batch.getListName();
        List<ShoppingRequestDto> results = batch.getItems().stream().map(req -> {
            if (req.getAssignedTo() == null) {
                req.setAssignedTo(finalAssignee);
            }
            return createRequestInternal(familyId, req, listName);
        }).toList();

        // Send single notification for batch
        if (finalAssignee != null) {
            User requester = userRepository.findById(userId).orElseThrow();
            eventPublisher.publishNotification(
                    AppEvent.of("shopping_batch_assigned", familyId, finalAssignee,
                            requester.getFullName() + " assigned you " + results.size() + " items to buy"));
        }

        return results;
    }

    @Transactional
    public ShoppingRequestDto quickPurchase(UUID familyId, UUID requestId, QuickPurchaseRequest purchase) {
        familyService.validateMembership(familyId);

        ShoppingRequest sr = shoppingRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingRequest", "id", requestId));

        if (!sr.getFamilyId().equals(familyId)) {
            throw new BadRequestException("Request does not belong to this family");
        }

        // Create purchase record
        CreatePurchaseRequest purchaseReq = new CreatePurchaseRequest();
        purchaseReq.setItemId(sr.getItem().getId());
        purchaseReq.setQuantity(purchase.getActualQuantity());
        purchaseReq.setUnitId(sr.getUnit().getId());
        purchaseReq.setCost(purchase.getCost());
        purchaseReq.setShopName(purchase.getShopName());
        purchaseReq.setNote(purchase.getNote());
        purchaseReq.setPurchaseDate(LocalDate.now());
        purchaseReq.setShoppingRequestId(sr.getId());

        purchaseService.createPurchase(familyId, purchaseReq);

        // Status already set to BOUGHT by PurchaseService
        sr = shoppingRepo.findById(requestId).orElseThrow();
        return toDto(sr);
    }

    @Transactional(readOnly = true)
    public List<ShoppingRequestDto> getRequests(UUID familyId, ShoppingStatus status) {
        familyService.validateMembership(familyId);
        if (status != null) {
            return shoppingRepo.findByFamilyIdAndStatus(familyId, status).stream().map(this::toDto).toList();
        }
        return shoppingRepo.findByFamilyId(familyId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ShoppingRequestDto> getMyTasks() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return shoppingRepo.findMyTasks(userId).stream().map(this::toDto).toList();
    }

    @Transactional
    public ShoppingRequestDto updateRequest(UUID familyId, UUID requestId, UpdateShoppingRequest update) {
        familyService.validateMembership(familyId);

        ShoppingRequest sr = shoppingRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingRequest", "id", requestId));

        if (!sr.getFamilyId().equals(familyId)) {
            throw new BadRequestException("Request does not belong to this family");
        }

        if (update.getStatus() != null) {
            sr.setStatus(update.getStatus());

            if (update.getStatus() == ShoppingStatus.BOUGHT) {
                eventPublisher.publishNotification(
                        AppEvent.of("item_bought", familyId, sr.getRequestedBy().getId(),
                                sr.getItem().getName() + " has been bought"));
            }
        }

        if (update.getAssignedTo() != null) {
            User assignee = userRepository.findById(update.getAssignedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", update.getAssignedTo()));
            sr.setAssignedTo(assignee);
        }

        sr = shoppingRepo.save(sr);
        return toDto(sr);
    }

    // === Private helpers ===

    private UUID resolveAutoAssignee(UUID familyId, UUID requesterId) {
        List<FamilyMember> members = memberRepository.findActiveMembersByFamilyId(familyId);
        if (members.size() == 2) {
            // Auto-assign to the other member
            return members.stream()
                    .filter(m -> !m.getUser().getId().equals(requesterId))
                    .findFirst()
                    .map(m -> m.getUser().getId())
                    .orElse(null);
        }
        return null; // more than 2 members — must explicitly assign
    }

    private Item resolveItem(UUID familyId, CreateShoppingRequest request) {
        if (request.getItemId() != null) {
            return itemRepository.findById(request.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item", "id", request.getItemId()));
        } else if (request.getItemName() != null && !request.getItemName().isBlank()) {
            ItemDto created = catalogService.getOrCreateItem(familyId, request.getItemName(),
                    request.getCategoryId(), request.getUnitId());
            return itemRepository.findById(created.getId()).orElseThrow();
        } else {
            throw new BadRequestException("Either itemId or itemName is required");
        }
    }

    private BigDecimal normalizeQuantity(BigDecimal quantity, Unit unit) {
        if (unit.getConversionFactor() != null) {
            return quantity.multiply(unit.getConversionFactor());
        }
        return quantity;
    }

    private Unit resolveDefaultUnit(Item item, UUID unitId) {
        if (unitId != null) {
            return catalogService.getUnitEntity(unitId);
        }
        if (item.getDefaultUnit() != null) {
            return item.getDefaultUnit();
        }
        throw new BadRequestException("Unit must be specified if no default unit is set for the item.");
    }

    private ShoppingRequestDto toDto(ShoppingRequest sr) {
        return ShoppingRequestDto.builder()
                .id(sr.getId())
                .familyId(sr.getFamilyId())
                .itemId(sr.getItem().getId())
                .itemName(sr.getItem().getName())
                .categoryName(sr.getItem().getCategory() != null ? sr.getItem().getCategory().getName() : null)
                .quantity(sr.getQuantity())
                .unitId(sr.getUnit().getId())
                .unitAbbreviation(sr.getUnit().getAbbreviation())
                .normalizedQuantity(sr.getNormalizedQuantity())
                .status(sr.getStatus().name())
                .priority(sr.getPriority())
                .dueDate(sr.getDueDate())
                .note(sr.getNote())
                .requestedById(sr.getRequestedBy().getId())
                .requestedByName(sr.getRequestedBy().getFullName())
                .assignedToId(sr.getAssignedTo() != null ? sr.getAssignedTo().getId() : null)
                .assignedToName(sr.getAssignedTo() != null ? sr.getAssignedTo().getFullName() : null)
                .listName(sr.getListName())
                .createdAt(sr.getCreatedAt())
                .build();
    }
}
