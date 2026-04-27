package com.familyhub.notification.repository;

import com.familyhub.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<Notification> findByUserIdAndReadStatusFalseOrderByCreatedAtDesc(UUID userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.readStatus = false")
    long countUnreadByUserId(UUID userId);

    @Modifying
    @Query("UPDATE Notification n SET n.readStatus = true WHERE n.userId = :userId AND n.readStatus = false")
    void markAllAsRead(UUID userId);
}
