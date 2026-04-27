package com.familyhub.common.event;

import com.familyhub.common.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishNotification(AppEvent event) {
        log.info("Publishing notification event: {}", event.getType());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "notification." + event.getType(), event);
    }

    public void publishInventoryUpdate(AppEvent event) {
        log.info("Publishing inventory event: {}", event.getType());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "inventory." + event.getType(), event);
    }

    public void publishReportUpdate(AppEvent event) {
        log.info("Publishing report event: {}", event.getType());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "report." + event.getType(), event);
    }
}
