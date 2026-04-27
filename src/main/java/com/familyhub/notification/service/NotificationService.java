package com.familyhub.notification.service;

import com.familyhub.common.security.SecurityUtils;
import com.familyhub.notification.dto.NotificationDto;
import com.familyhub.notification.entity.Notification;
import com.familyhub.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepo;
    private final SseEmitterService sseEmitterService;

    public void createNotification(UUID userId, UUID familyId, String type, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .familyId(familyId)
                .type(type)
                .message(message)
                .build();
        notificationRepo.save(notification);
        log.debug("Notification created for user {}: {}", userId, type);

        // Push real-time SSE event to the connected user
        sseEmitterService.sendToUser(userId, "notification", toDto(notification));
    }

    public List<NotificationDto> getMyNotifications() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return notificationRepo.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto).toList();
    }

    public List<NotificationDto> getUnread() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return notificationRepo.findByUserIdAndReadStatusFalseOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto).toList();
    }

    @Transactional
    public void markAllRead() {
        UUID userId = SecurityUtils.getCurrentUserId();
        notificationRepo.markAllAsRead(userId);
    }

    @Transactional
    public void markRead(UUID notificationId) {
        notificationRepo.findById(notificationId).ifPresent(n -> {
            n.setReadStatus(true);
            notificationRepo.save(n);
        });
    }

    private NotificationDto toDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .type(n.getType())
                .message(n.getMessage())
                .read(n.isReadStatus())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
