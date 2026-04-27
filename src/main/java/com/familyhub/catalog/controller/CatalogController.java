package com.familyhub.catalog.controller;

import com.familyhub.catalog.dto.*;
import com.familyhub.catalog.service.CatalogService;
import com.familyhub.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Catalog")
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/units")
    @Operation(summary = "Get all units")
    public ResponseEntity<ApiResponse<List<UnitDto>>> getUnits() {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getAllUnits()));
    }

    @GetMapping("/families/{familyId}/categories")
    @Operation(summary = "Get categories for a family")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategories(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getCategories(familyId)));
    }

    @GetMapping("/families/{familyId}/items")
    @Operation(summary = "Get all items for a family")
    public ResponseEntity<ApiResponse<List<ItemDto>>> getItems(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getAllItems(familyId)));
    }

    @GetMapping("/families/{familyId}/items/search")
    @Operation(summary = "Search items")
    public ResponseEntity<ApiResponse<List<ItemDto>>> searchItems(
            @PathVariable UUID familyId,
            @RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success(catalogService.searchItems(familyId, q)));
    }

    @PostMapping("/families/{familyId}/items")
    @Operation(summary = "Create a new item")
    public ResponseEntity<ApiResponse<ItemDto>> createItem(
            @PathVariable UUID familyId,
            @Valid @RequestBody CreateItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item created", catalogService.createItem(familyId, request)));
    }
}
