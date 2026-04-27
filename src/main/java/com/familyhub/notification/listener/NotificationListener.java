package com.familyhub.notification.listener;

import com.familyhub.common.config.RabbitMQConfig;
import com.familyhub.common.event.AppEvent;
import com.familyhub.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(AppEvent event) {
        log.info("Received notification event: {} for user: {}", event.getType(), event.getUserId());
        try {
            notificationService.createNotification(
                    event.getUserId(),
                    event.getFamilyId(),
                    event.getType(),
                    event.getMessage());
        } catch (Exception e) {
            log.error("Error processing notification event: {}", event.getEventId(), e);
            throw e; // triggers retry / DLQ
        }
    }
}
