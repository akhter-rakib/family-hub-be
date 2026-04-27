package com.familyhub.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SseEmitterService {

    private static final long SSE_TIMEOUT = 30 * 60 * 1000L; // 30 minutes

    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(UUID userId) {
        // Remove any existing emitter for this user
        removeEmitter(userId);

        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);

        emitter.onCompletion(() -> {
            log.debug("SSE connection completed for user {}", userId);
            emitters.remove(userId);
        });
        emitter.onTimeout(() -> {
            log.debug("SSE connection timed out for user {}", userId);
            emitters.remove(userId);
        });
        emitter.onError(e -> {
            log.debug("SSE connection error for user {}: {}", userId, e.getMessage());
            emitters.remove(userId);
        });

        emitters.put(userId, emitter);
        log.debug("SSE emitter created for user {}", userId);
        return emitter;
    }

    public void sendToUser(UUID userId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            } catch (IOException e) {
                log.debug("Failed to send SSE to user {}, removing emitter", userId);
                emitters.remove(userId);
            }
        }
    }

    private void removeEmitter(UUID userId) {
        SseEmitter existing = emitters.remove(userId);
        if (existing != null) {
            existing.complete();
        }
    }
}
