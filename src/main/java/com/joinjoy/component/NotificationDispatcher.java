package com.joinjoy.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.joinjoy.dto.NotificationDTO;

@Component
public class NotificationDispatcher {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

    public void dispatch(List<NotificationDTO> notificationDTOs) {
        for (SseEmitter emitter : new ArrayList<>(emitters)) {
            try {
                emitter.send(notificationDTOs);
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }
    }
}
