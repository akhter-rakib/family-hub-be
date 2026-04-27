package com.familyhub.notification.controller;

import com.familyhub.common.dto.ApiResponse;
import com.familyhub.notification.dto.NotificationDto;
import com.familyhub.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get all notifications")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getMyNotifications()));
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getUnread() {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getUnread()));
    }

    @PatchMapping("/read-all")
    @Operation(summary = "Mark all as read")
    public ResponseEntity<ApiResponse<Void>> markAllRead() {
        notificationService.markAllRead();
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read"));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<ApiResponse<Void>> markRead(@PathVariable UUID id) {
        notificationService.markRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read"));
    }
}
