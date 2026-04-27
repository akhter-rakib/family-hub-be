package com.familyhub.inventory.controller;

import com.familyhub.common.dto.ApiResponse;
import com.familyhub.inventory.dto.*;
import com.familyhub.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/families/{familyId}/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @PutMapping
    @Operation(summary = "Add or update inventory")
    public ResponseEntity<ApiResponse<InventoryItemDto>> upsert(
            @PathVariable UUID familyId, @Valid @RequestBody UpsertInventoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.upsertInventory(familyId, request)));
    }

    @GetMapping
    @Operation(summary = "Get inventory")
    public ResponseEntity<ApiResponse<List<InventoryItemDto>>> getInventory(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.getInventory(familyId)));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock items")
    public ResponseEntity<ApiResponse<List<InventoryItemDto>>> getLowStock(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.getLowStock(familyId)));
    }

    @GetMapping("/expiring")
    @Operation(summary = "Get expiring items")
    public ResponseEntity<ApiResponse<List<InventoryItemDto>>> getExpiring(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.getExpiringSoon(familyId)));
    }
}
