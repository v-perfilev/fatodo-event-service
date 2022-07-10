package com.persoff68.fatodo.web.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.exception.KafkaException;
import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.ReminderEvent;
import com.persoff68.fatodo.model.dto.ChatEventDTO;
import com.persoff68.fatodo.model.dto.CommentEventDTO;
import com.persoff68.fatodo.model.dto.ContactEventDTO;
import com.persoff68.fatodo.model.dto.DeleteChatEventsDTO;
import com.persoff68.fatodo.model.dto.DeleteContactEventsDTO;
import com.persoff68.fatodo.model.dto.DeleteEventsDTO;
import com.persoff68.fatodo.model.dto.DeleteGroupEventsDTO;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.ItemEventDTO;
import com.persoff68.fatodo.model.dto.ReminderEventDTO;
import com.persoff68.fatodo.model.mapper.EventMapper;
import com.persoff68.fatodo.service.EventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Component
@RequiredArgsConstructor
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class EventConsumer {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final ObjectMapper objectMapper;

    @Getter
    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "event_add", containerFactory = "addContainerFactory")
    public void addEvent(@Payload String value, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        switch (key) {
            case "default" -> handleDefaultEvent(value);
            case "contact" -> handleContactEvent(value);
            case "item" -> handleItemEvent(value);
            case "comment" -> handleCommentEvent(value);
            case "chat" -> handleChatEvent(value);
            case "reminder" -> handleReminderEvent(value);
            default -> throw new KafkaException();
        }
        resetLatch();
    }

    @KafkaListener(topics = "event_delete", containerFactory = "deleteContainerFactory")
    public void deleteEvent(@Payload String value, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        switch (key) {
            case "group-delete-user" -> handleDeleteGroupEventsForUser(value);
            case "chat-delete-user" -> handleDeleteChatEventsForUser(value);
            case "contact-delete" -> handleDeleteContactEvents(value);
            case "group-delete" -> handleDeleteGroupEvents(value);
            case "item-delete" -> handleDeleteItemEvents(value);
            case "chat-delete" -> handleDeleteChatEvents(value);
            default -> throw new KafkaException();
        }
        resetLatch();
    }


    private void handleDefaultEvent(String value) {
        try {
            EventDTO dto = objectMapper.readValue(value, EventDTO.class);
            eventService.addDefaultEvent(dto.getType(), dto.getRecipientIds());
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }

    private void handleContactEvent(String value) {
        try {
            ContactEventDTO dto = objectMapper.readValue(value, ContactEventDTO.class);
            ContactEvent contactEvent = eventMapper.contactDTOToPojo(dto);
            eventService.addContactEvent(dto.getType(), dto.getRecipientIds(), contactEvent);
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }

    private void handleItemEvent(String value) {
        try {
            ItemEventDTO dto = objectMapper.readValue(value, ItemEventDTO.class);
            ItemEvent itemEvent = eventMapper.itemDTOToPojo(dto);
            eventService.addItemEvent(dto.getType(), dto.getRecipientIds(), itemEvent, dto.getUserIds());
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }

    private void handleCommentEvent(String value) {
        try {
            CommentEventDTO dto = objectMapper.readValue(value, CommentEventDTO.class);
            CommentEvent commentEvent = eventMapper.commentDTOToPojo(dto);
            eventService.addCommentEvent(dto.getType(), dto.getRecipientIds(), commentEvent);
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }

    private void handleChatEvent(String value) {
        try {
            ChatEventDTO dto = objectMapper.readValue(value, ChatEventDTO.class);
            ChatEvent chatEvent = eventMapper.chatDTOToPojo(dto);
            eventService.addChatEvent(dto.getType(), dto.getRecipientIds(), chatEvent, dto.getUserIds());
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }

    private void handleReminderEvent(String value) {
        try {
            ReminderEventDTO dto = objectMapper.readValue(value, ReminderEventDTO.class);
            ReminderEvent reminderEvent = eventMapper.reminderDTOToPojo(dto);
            eventService.addReminderEvent(dto.getType(), dto.getRecipientIds(), reminderEvent);
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }


    private void handleDeleteGroupEventsForUser(String value) {
        try {
            DeleteGroupEventsDTO dto = objectMapper.readValue(value, DeleteGroupEventsDTO.class);
            List<UUID> userIdList = List.of(dto.getUserId());
            eventService.deleteGroupAndCommentEventsForUser(dto.getGroupId(), userIdList);
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }

    private void handleDeleteChatEventsForUser(String value) {
        try {
            DeleteChatEventsDTO dto = objectMapper.readValue(value, DeleteChatEventsDTO.class);
            List<UUID> userIdList = List.of(dto.getUserId());
            eventService.deleteChatEventsForUser(dto.getChatId(), userIdList);
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }

    private void handleDeleteContactEvents(String value) {
        try {
            DeleteContactEventsDTO dto = objectMapper.readValue(value, DeleteContactEventsDTO.class);
            List<UUID> userIdList = List.of(dto.getFirstUserId(), dto.getSecondUserId());
            eventService.deleteContactsEvents(userIdList);
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }

    private void handleDeleteGroupEvents(String value) {
        try {
            DeleteEventsDTO dto = objectMapper.readValue(value, DeleteEventsDTO.class);
            eventService.deleteGroupAndCommentEvents(dto.getId());
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }

    private void handleDeleteItemEvents(String value) {
        try {
            DeleteEventsDTO dto = objectMapper.readValue(value, DeleteEventsDTO.class);
            eventService.deleteItemAndCommentEvents(dto.getId());
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }

    private void handleDeleteChatEvents(String value) {
        try {
            DeleteEventsDTO dto = objectMapper.readValue(value, DeleteEventsDTO.class);
            eventService.deleteChatEvents(dto.getId());
        } catch (JsonProcessingException e) {
            throw new KafkaException();
        }
    }


    private void resetLatch() {
        this.latch.countDown();
        this.latch = new CountDownLatch(1);
    }

}
