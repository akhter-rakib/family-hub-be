package com.familyhub.desco.controller;

import com.familyhub.common.dto.ApiResponse;
import com.familyhub.desco.dto.DescoBalanceDto;
import com.familyhub.desco.service.DescoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/families/{familyId}/desco")
@RequiredArgsConstructor
@Tag(name = "DESCO")
public class DescoController {

    private final DescoService descoService;

    @GetMapping("/balance")
    @Operation(summary = "Get latest DESCO prepaid balance for family")
    public ResponseEntity<ApiResponse<DescoBalanceDto>> getLatestBalance(@PathVariable java.util.UUID familyId) {
        DescoBalanceDto balance = descoService.getLatestBalance(familyId);
        return ResponseEntity.ok(ApiResponse.success(balance));
    }

    @PostMapping("/balance/refresh")
    @Operation(summary = "Manually trigger DESCO balance fetch for family")
    public ResponseEntity<ApiResponse<DescoBalanceDto>> refreshBalance(@PathVariable java.util.UUID familyId) {
        descoService.fetchAndSaveBalanceForFamily(familyId);
        DescoBalanceDto balance = descoService.getLatestBalance(familyId);
        return ResponseEntity.ok(ApiResponse.success("Balance refreshed", balance));
    }
}
