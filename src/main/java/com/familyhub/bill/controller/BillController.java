package com.familyhub.bill.controller;

import com.familyhub.bill.dto.*;
import com.familyhub.bill.service.BillService;
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
@RequestMapping("/families/{familyId}/bills")
@RequiredArgsConstructor
@Tag(name = "Bills")
public class BillController {

    private final BillService billService;

    @PostMapping
    @Operation(summary = "Create a bill")
    public ResponseEntity<ApiResponse<BillDto>> create(
            @PathVariable UUID familyId, @Valid @RequestBody CreateBillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bill created", billService.createBill(familyId, request)));
    }

    @GetMapping
    @Operation(summary = "Get all bills")
    public ResponseEntity<ApiResponse<List<BillDto>>> getBills(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(billService.getBills(familyId)));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue bills")
    public ResponseEntity<ApiResponse<List<BillDto>>> getOverdue(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(billService.getOverdueBills(familyId)));
    }

    @PatchMapping("/{billId}/pay")
    @Operation(summary = "Mark bill as paid")
    public ResponseEntity<ApiResponse<BillDto>> markPaid(
            @PathVariable UUID familyId, @PathVariable UUID billId) {
        return ResponseEntity.ok(ApiResponse.success("Bill marked as paid", billService.markPaid(familyId, billId)));
    }
}
