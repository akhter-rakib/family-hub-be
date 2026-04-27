package com.familyhub.catalog.service;

import com.familyhub.catalog.dto.*;
import com.familyhub.catalog.entity.*;
import com.familyhub.catalog.repository.*;
import com.familyhub.common.exception.ResourceNotFoundException;
import com.familyhub.family.service.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogService {

    private final UnitRepository unitRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final FamilyService familyService;

    @Cacheable("units")
    public List<UnitDto> getAllUnits() {
        return unitRepository.findAll().stream().map(this::toUnitDto).toList();
    }

    @Cacheable(value = "categories", key = "#familyId")
    public List<CategoryDto> getCategories(UUID familyId) {
        familyService.validateMembership(familyId);
        return categoryRepository.findAllByFamilyIdOrGlobal(familyId).stream()
                .map(this::toCategoryDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemDto> searchItems(UUID familyId, String query) {
        familyService.validateMembership(familyId);
        return itemRepository.searchItems(familyId, query).stream()
                .map(this::toItemDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getAllItems(UUID familyId) {
        familyService.validateMembership(familyId);
        return itemRepository.findAllByFamilyIdOrGlobal(familyId).stream()
                .map(this::toItemDto)
                .toList();
    }

    @Transactional
    @CacheEvict(value = "items", allEntries = true)
    public ItemDto createItem(UUID familyId, CreateItemRequest request) {
        familyService.validateMembership(familyId);

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        }

        Unit defaultUnit = null;
        if (request.getDefaultUnitId() != null) {
            defaultUnit = unitRepository.findById(request.getDefaultUnitId()).orElse(null);
        }

        Item item = Item.builder()
                .name(request.getName())
                .category(category)
                .defaultUnit(defaultUnit)
                .familyId(familyId)
                .build();

        item = itemRepository.save(item);
        log.info("Item created: {} for family: {}", item.getName(), familyId);
        return toItemDto(item);
    }

    public ItemDto getOrCreateItem(UUID familyId, String itemName, UUID categoryId, UUID unitId) {
        List<Item> existing = itemRepository.searchItems(familyId, itemName);
        Item exactMatch = existing.stream()
                .filter(i -> i.getName().equalsIgnoreCase(itemName))
                .findFirst()
                .orElse(null);

        if (exactMatch != null) return toItemDto(exactMatch);

        CreateItemRequest req = new CreateItemRequest();
        req.setName(itemName);
        req.setCategoryId(categoryId);
        req.setDefaultUnitId(unitId);
        return createItem(familyId, req);
    }

    public Unit getUnitEntity(UUID unitId) {
        return unitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", unitId));
    }

    private UnitDto toUnitDto(Unit unit) {
        return UnitDto.builder()
                .id(unit.getId())
                .name(unit.getName())
                .abbreviation(unit.getAbbreviation())
                .baseUnit(unit.getBaseUnit())
                .conversionFactor(unit.getConversionFactor())
                .unitType(unit.getUnitType())
                .build();
    }

    private CategoryDto toCategoryDto(Category cat) {
        return CategoryDto.builder()
                .id(cat.getId())
                .name(cat.getName())
                .icon(cat.getIcon())
                .build();
    }

    private ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .categoryId(item.getCategory() != null ? item.getCategory().getId() : null)
                .categoryName(item.getCategory() != null ? item.getCategory().getName() : null)
                .defaultUnitId(item.getDefaultUnit() != null ? item.getDefaultUnit().getId() : null)
                .defaultUnitName(item.getDefaultUnit() != null ? item.getDefaultUnit().getName() : null)
                .global(item.getFamilyId() == null)
                .build();
    }
}
