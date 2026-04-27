package com.familyhub.family.service;

import com.familyhub.auth.entity.User;
import com.familyhub.auth.repository.UserRepository;
import com.familyhub.auth.service.AuthService;
import com.familyhub.common.event.AppEvent;
import com.familyhub.common.event.EventPublisher;
import com.familyhub.common.exception.BadRequestException;
import com.familyhub.common.exception.ForbiddenException;
import com.familyhub.common.exception.ResourceNotFoundException;
import com.familyhub.common.security.SecurityUtils;
import com.familyhub.family.dto.*;
import com.familyhub.family.entity.*;
import com.familyhub.family.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository memberRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    @Transactional
    public FamilyDto createFamily(CreateFamilyRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Family family = Family.builder()
                .name(request.getName())
                .description(request.getDescription())
                .inviteCode(generateInviteCode())
                .build();
        family = familyRepository.save(family);

        FamilyMember member = FamilyMember.builder()
                .family(family)
                .user(user)
                .role(FamilyRole.OWNER)
                .build();
        memberRepository.save(member);

        log.info("Family created: {} by user: {}", family.getName(), user.getEmail());

        return toFamilyDto(family, FamilyRole.OWNER, 1);
    }

    public List<FamilyDto> getMyFamilies() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return memberRepository.findActiveMembershipsByUserId(userId).stream()
                .map(fm -> {
                    int count = memberRepository.findActiveMembersByFamilyId(fm.getFamily().getId()).size();
                    return toFamilyDto(fm.getFamily(), fm.getRole(), count);
                })
                .toList();
    }

    public FamilyDto getFamily(UUID familyId) {
        validateMembership(familyId);
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family", "id", familyId));
        FamilyRole role = memberRepository.findRoleByFamilyIdAndUserId(familyId, SecurityUtils.getCurrentUserId())
                .orElseThrow();
        int count = memberRepository.findActiveMembersByFamilyId(familyId).size();
        return toFamilyDto(family, role, count);
    }

    public List<FamilyMemberDto> getMembers(UUID familyId) {
        validateMembership(familyId);
        return memberRepository.findActiveMembersByFamilyId(familyId).stream()
                .map(this::toMemberDto)
                .toList();
    }

    @Transactional
    public void submitJoinRequest(JoinFamilyRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        Family family = familyRepository.findByInviteCode(request.getInviteCode())
                .orElseThrow(() -> new ResourceNotFoundException("Family", "inviteCode", request.getInviteCode()));

        if (memberRepository.existsByFamilyIdAndUserIdAndActiveTrue(family.getId(), userId)) {
            throw new BadRequestException("Already a member of this family");
        }

        if (joinRequestRepository.existsByFamilyIdAndUserIdAndStatus(family.getId(), userId, JoinRequestStatus.PENDING)) {
            throw new BadRequestException("Join request already pending");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        FamilyJoinRequest joinReq = FamilyJoinRequest.builder()
                .family(family)
                .user(user)
                .message(request.getMessage())
                .build();
        joinRequestRepository.save(joinReq);

        eventPublisher.publishNotification(
                AppEvent.of("join_request", family.getId(), userId,
                        user.getFullName() + " requested to join " + family.getName()));

        log.info("Join request submitted for family: {} by user: {}", family.getName(), user.getEmail());
    }

    public List<JoinRequestDto> getPendingRequests(UUID familyId) {
        validateAdminAccess(familyId);
        return joinRequestRepository.findByFamilyIdAndStatus(familyId, JoinRequestStatus.PENDING).stream()
                .map(this::toJoinRequestDto)
                .toList();
    }

    @Transactional
    public void approveRequest(UUID familyId, UUID requestId) {
        validateAdminAccess(familyId);

        FamilyJoinRequest joinReq = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("JoinRequest", "id", requestId));

        if (joinReq.getStatus() != JoinRequestStatus.PENDING) {
            throw new BadRequestException("Request is not pending");
        }

        joinReq.setStatus(JoinRequestStatus.APPROVED);
        joinRequestRepository.save(joinReq);

        FamilyMember member = FamilyMember.builder()
                .family(joinReq.getFamily())
                .user(joinReq.getUser())
                .role(FamilyRole.MEMBER)
                .build();
        memberRepository.save(member);

        eventPublisher.publishNotification(
                AppEvent.of("request_approved", familyId, joinReq.getUser().getId(),
                        "Your request to join " + joinReq.getFamily().getName() + " was approved"));

        log.info("Join request approved for user: {}", joinReq.getUser().getEmail());
    }

    @Transactional
    public void rejectRequest(UUID familyId, UUID requestId) {
        validateAdminAccess(familyId);

        FamilyJoinRequest joinReq = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("JoinRequest", "id", requestId));

        joinReq.setStatus(JoinRequestStatus.REJECTED);
        joinRequestRepository.save(joinReq);
    }

    public void validateMembership(UUID familyId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!memberRepository.existsByFamilyIdAndUserIdAndActiveTrue(familyId, userId)) {
            throw new ForbiddenException("You are not a member of this family");
        }
    }

    private void validateAdminAccess(UUID familyId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        FamilyRole role = memberRepository.findRoleByFamilyIdAndUserId(familyId, userId)
                .orElseThrow(() -> new ForbiddenException("You are not a member of this family"));

        if (role != FamilyRole.OWNER && role != FamilyRole.ADMIN) {
            throw new ForbiddenException("Admin or Owner access required");
        }
    }

    private String generateInviteCode() {
        StringBuilder code = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            code.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
        }
        return code.toString();
    }

    private FamilyDto toFamilyDto(Family family, FamilyRole role, int memberCount) {
        return FamilyDto.builder()
                .id(family.getId())
                .name(family.getName())
                .description(family.getDescription())
                .inviteCode(family.getInviteCode())
                .myRole(role.name())
                .memberCount(memberCount)
                .createdAt(family.getCreatedAt())
                .build();
    }

    private FamilyMemberDto toMemberDto(FamilyMember fm) {
        return FamilyMemberDto.builder()
                .id(fm.getId())
                .userId(fm.getUser().getId())
                .name(fm.getUser().getFullName())
                .email(fm.getUser().getEmail())
                .role(fm.getRole())
                .joinedAt(fm.getCreatedAt())
                .build();
    }

    private JoinRequestDto toJoinRequestDto(FamilyJoinRequest jr) {
        return JoinRequestDto.builder()
                .id(jr.getId())
                .userId(jr.getUser().getId())
                .userName(jr.getUser().getFullName())
                .userEmail(jr.getUser().getEmail())
                .status(jr.getStatus().name())
                .message(jr.getMessage())
                .createdAt(jr.getCreatedAt())
                .build();
    }
}
