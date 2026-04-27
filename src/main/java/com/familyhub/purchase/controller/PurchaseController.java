package com.familyhub.purchase.controller;

import com.familyhub.common.dto.ApiResponse;
import com.familyhub.purchase.dto.*;
import com.familyhub.purchase.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/families/{familyId}/purchases")
@RequiredArgsConstructor
@Tag(name = "Purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    @Operation(summary = "Record a purchase")
    public ResponseEntity<ApiResponse<PurchaseDto>> create(
            @PathVariable UUID familyId,
            @Valid @RequestBody CreatePurchaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Purchase recorded", purchaseService.createPurchase(familyId, request)));
    }

    @GetMapping
    @Operation(summary = "Get purchases")
    public ResponseEntity<ApiResponse<List<PurchaseDto>>> getPurchases(
            @PathVariable UUID familyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from != null && to != null) {
            return ResponseEntity.ok(ApiResponse.success(purchaseService.getPurchasesByDateRange(familyId, from, to)));
        }
        return ResponseEntity.ok(ApiResponse.success(purchaseService.getPurchases(familyId)));
    }
}
