package com.familyhub.family.controller;

import com.familyhub.common.dto.ApiResponse;
import com.familyhub.family.dto.*;
import com.familyhub.family.service.FamilyService;
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
@RequestMapping("/families")
@RequiredArgsConstructor
@Tag(name = "Family")
public class FamilyController {

    private final FamilyService familyService;

    @PostMapping
    @Operation(summary = "Create a family")
    public ResponseEntity<ApiResponse<FamilyDto>> create(@Valid @RequestBody CreateFamilyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Family created", familyService.createFamily(request)));
    }

    @GetMapping
    @Operation(summary = "Get my families")
    public ResponseEntity<ApiResponse<List<FamilyDto>>> getMyFamilies() {
        return ResponseEntity.ok(ApiResponse.success(familyService.getMyFamilies()));
    }

    @GetMapping("/{familyId}")
    @Operation(summary = "Get family details")
    public ResponseEntity<ApiResponse<FamilyDto>> getFamily(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(familyService.getFamily(familyId)));
    }

    @GetMapping("/{familyId}/members")
    @Operation(summary = "Get family members")
    public ResponseEntity<ApiResponse<List<FamilyMemberDto>>> getMembers(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(familyService.getMembers(familyId)));
    }

    @PostMapping("/join-request")
    @Operation(summary = "Submit join request")
    public ResponseEntity<ApiResponse<Void>> joinRequest(@Valid @RequestBody JoinFamilyRequest request) {
        familyService.submitJoinRequest(request);
        return ResponseEntity.ok(ApiResponse.success("Join request submitted"));
    }

    @GetMapping("/{familyId}/join-requests")
    @Operation(summary = "Get pending join requests")
    public ResponseEntity<ApiResponse<List<JoinRequestDto>>> getPendingRequests(@PathVariable UUID familyId) {
        return ResponseEntity.ok(ApiResponse.success(familyService.getPendingRequests(familyId)));
    }

    @PostMapping("/{familyId}/join-requests/{requestId}/approve")
    @Operation(summary = "Approve join request")
    public ResponseEntity<ApiResponse<Void>> approve(@PathVariable UUID familyId, @PathVariable UUID requestId) {
        familyService.approveRequest(familyId, requestId);
        return ResponseEntity.ok(ApiResponse.success("Request approved"));
    }

    @PostMapping("/{familyId}/join-requests/{requestId}/reject")
    @Operation(summary = "Reject join request")
    public ResponseEntity<ApiResponse<Void>> reject(@PathVariable UUID familyId, @PathVariable UUID requestId) {
        familyService.rejectRequest(familyId, requestId);
        return ResponseEntity.ok(ApiResponse.success("Request rejected"));
    }
}
