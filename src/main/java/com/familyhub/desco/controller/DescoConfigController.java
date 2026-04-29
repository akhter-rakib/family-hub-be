package com.familyhub.desco.controller;

import com.familyhub.common.dto.ApiResponse;
import com.familyhub.desco.dto.DescoConfigDto;
import com.familyhub.desco.service.DescoConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/families/{familyId}/desco-config")
@RequiredArgsConstructor
@Tag(name = "DESCO Config")
public class DescoConfigController {
    private final DescoConfigService descoConfigService;

    @GetMapping
    @Operation(summary = "Get DESCO config for family")
    public ResponseEntity<ApiResponse<DescoConfigDto>> get(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(descoConfigService.getConfig(familyId).orElse(null)));
    }

    @PostMapping
    @Operation(summary = "Create or update DESCO config for family")
    public ResponseEntity<ApiResponse<DescoConfigDto>> save(@PathVariable UUID familyId, @RequestBody DescoConfigDto dto) {
        DescoConfigDto saved = descoConfigService.saveOrUpdate(familyId, dto.getAccountNo(), dto.getMeterNo(), dto.isEnabled());
        return ResponseEntity.ok(ApiResponse.success(saved));
    }

    @DeleteMapping
    @Operation(summary = "Delete DESCO config for family")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID familyId) {
        descoConfigService.delete(familyId);
        return ResponseEntity.ok(ApiResponse.success((Void) null));
    }
}
