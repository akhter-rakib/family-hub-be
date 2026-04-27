package com.familyhub.shopping.controller;

import com.familyhub.common.dto.ApiResponse;
import com.familyhub.shopping.dto.*;
import com.familyhub.shopping.entity.ShoppingStatus;
import com.familyhub.shopping.service.ShoppingService;
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
@RequestMapping("/families/{familyId}/shopping-requests")
@RequiredArgsConstructor
@Tag(name = "Shopping")
public class ShoppingController {

    private final ShoppingService shoppingService;

    @PostMapping
    @Operation(summary = "Create shopping request")
    public ResponseEntity<ApiResponse<ShoppingRequestDto>> create(
            @PathVariable UUID familyId,
            @Valid @RequestBody CreateShoppingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Request created", shoppingService.createRequest(familyId, request)));
    }

    @PostMapping("/batch")
    @Operation(summary = "Create multiple shopping requests at once")
    public ResponseEntity<ApiResponse<List<ShoppingRequestDto>>> batchCreate(
            @PathVariable UUID familyId,
            @Valid @RequestBody BatchCreateShoppingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Requests created", shoppingService.batchCreate(familyId, request)));
    }

    @GetMapping
    @Operation(summary = "Get shopping requests")
    public ResponseEntity<ApiResponse<List<ShoppingRequestDto>>> getRequests(
            @PathVariable UUID familyId,
            @RequestParam(required = false) ShoppingStatus status) {
        return ResponseEntity.ok(ApiResponse.success(shoppingService.getRequests(familyId, status)));
    }

    @GetMapping("/my-tasks")
    @Operation(summary = "Get my assigned tasks")
    public ResponseEntity<ApiResponse<List<ShoppingRequestDto>>> getMyTasks() {
        return ResponseEntity.ok(ApiResponse.success(shoppingService.getMyTasks()));
    }

    @PatchMapping("/{requestId}")
    @Operation(summary = "Update shopping request")
    public ResponseEntity<ApiResponse<ShoppingRequestDto>> update(
            @PathVariable UUID familyId,
            @PathVariable UUID requestId,
            @RequestBody UpdateShoppingRequest update) {
        return ResponseEntity.ok(ApiResponse.success(shoppingService.updateRequest(familyId, requestId, update)));
    }

    @PostMapping("/{requestId}/quick-purchase")
    @Operation(summary = "Mark item as bought with price — creates purchase record automatically")
    public ResponseEntity<ApiResponse<ShoppingRequestDto>> quickPurchase(
            @PathVariable UUID familyId,
            @PathVariable UUID requestId,
            @Valid @RequestBody QuickPurchaseRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Purchased!", shoppingService.quickPurchase(familyId, requestId, request)));
    }
}
