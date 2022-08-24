package com.persoff68.fatodo.web.kafka;

import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.service.EventService;
import com.persoff68.fatodo.service.EventServiceFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@RequiredArgsConstructor
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class EventConsumer {

    private static final String EVENT_TOPIC = "event";

    private final EventServiceFactory eventServiceFactory;

    @Getter
    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = EVENT_TOPIC, containerFactory = "eventContainerFactory")
    public void addEvent(@Payload EventDTO eventDTO) {
        EventService eventService = eventServiceFactory.create(eventDTO.getType());
        eventService.addEvent(eventDTO);
        resetLatch();
    }

    private void resetLatch() {
        this.latch.countDown();
        this.latch = new CountDownLatch(1);
    }

}
