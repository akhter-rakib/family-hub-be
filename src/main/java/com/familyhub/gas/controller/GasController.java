package com.familyhub.gas.controller;

import com.familyhub.common.dto.ApiResponse;
import com.familyhub.gas.dto.*;
import com.familyhub.gas.service.GasService;
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
@RequestMapping("/families/{familyId}/gas")
@RequiredArgsConstructor
@Tag(name = "Gas")
public class GasController {

    private final GasService gasService;

    @PostMapping
    @Operation(summary = "Start gas usage tracking")
    public ResponseEntity<ApiResponse<GasUsageDto>> start(
            @PathVariable UUID familyId, @Valid @RequestBody CreateGasUsageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Gas tracking started", gasService.startGasUsage(familyId, request)));
    }

    @PatchMapping("/{gasId}")
    @Operation(summary = "Update a gas log")
    public ResponseEntity<ApiResponse<GasUsageDto>> update(
            @PathVariable UUID familyId, @PathVariable UUID gasId,
            @RequestBody UpdateGasRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Gas log updated", gasService.updateGasUsage(familyId, gasId, request)));
    }

    @PatchMapping("/{gasId}/finish")
    @Operation(summary = "Finish gas usage")
    public ResponseEntity<ApiResponse<GasUsageDto>> finish(
            @PathVariable UUID familyId, @PathVariable UUID gasId) {
        return ResponseEntity.ok(ApiResponse.success("Gas usage finished", gasService.finishGasUsage(familyId, gasId)));
    }

    @GetMapping
    @Operation(summary = "Get gas usage logs")
    public ResponseEntity<ApiResponse<List<GasUsageDto>>> getLogs(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(gasService.getGasUsageLogs(familyId)));
    }

    @GetMapping("/available-cylinders")
    @Operation(summary = "Get purchased gas cylinders not yet started")
    public ResponseEntity<ApiResponse<List<AvailableCylinderDto>>> getAvailableCylinders(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(gasService.getAvailableCylinders(familyId)));
    }
}
