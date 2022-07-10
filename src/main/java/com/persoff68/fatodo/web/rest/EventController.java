package com.persoff68.fatodo.web.rest;

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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(EventController.ENDPOINT)
@RequiredArgsConstructor
public class EventController {
    static final String ENDPOINT = "/api/events";

    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping("/default")
    public ResponseEntity<Void> addDefaultEvent(@RequestBody EventDTO dto) {
        eventService.addDefaultEvent(dto.getType(), dto.getRecipientIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/contact")
    public ResponseEntity<Void> addContactEvent(@RequestBody ContactEventDTO dto) {
        ContactEvent contactEvent = eventMapper.contactDTOToPojo(dto);
        eventService.addContactEvent(dto.getType(), dto.getRecipientIds(), contactEvent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/item")
    public ResponseEntity<Void> addItemEvent(@RequestBody ItemEventDTO dto) {
        ItemEvent itemEvent = eventMapper.itemDTOToPojo(dto);
        eventService.addItemEvent(dto.getType(), dto.getRecipientIds(), itemEvent, dto.getUserIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comment")
    public ResponseEntity<Void> addCommentEvent(@RequestBody CommentEventDTO dto) {
        CommentEvent commentEvent = eventMapper.commentDTOToPojo(dto);
        eventService.addCommentEvent(dto.getType(), dto.getRecipientIds(), commentEvent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/chat")
    public ResponseEntity<Void> addChatEvent(@RequestBody ChatEventDTO dto) {
        ChatEvent chatEvent = eventMapper.chatDTOToPojo(dto);
        eventService.addChatEvent(dto.getType(), dto.getRecipientIds(), chatEvent, dto.getUserIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/reminder")
    public ResponseEntity<Void> addReminderEvent(@RequestBody ReminderEventDTO dto) {
        ReminderEvent reminderEvent = eventMapper.reminderDTOToPojo(dto);
        eventService.addReminderEvent(dto.getType(), dto.getRecipientIds(), reminderEvent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/group/delete-user")
    public ResponseEntity<Void> deleteGroupAndCommentEventsForUser(@RequestBody DeleteGroupEventsDTO dto) {
        List<UUID> userIdList = List.of(dto.getUserId());
        eventService.deleteGroupAndCommentEventsForUser(dto.getGroupId(), userIdList);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/chat/delete-user")
    public ResponseEntity<Void> deleteChatEventsForUser(@RequestBody DeleteChatEventsDTO dto) {
        List<UUID> userIdList = List.of(dto.getUserId());
        eventService.deleteChatEventsForUser(dto.getChatId(), userIdList);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/contact/delete")
    public ResponseEntity<Void> deleteContactEvents(@RequestBody DeleteContactEventsDTO dto) {
        List<UUID> userIdList = List.of(dto.getFirstUserId(), dto.getSecondUserId());
        eventService.deleteContactsEvents(userIdList);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/group/delete")
    public ResponseEntity<Void> deleteGroupEvents(@RequestBody DeleteEventsDTO dto) {
        eventService.deleteGroupAndCommentEvents(dto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/item/delete")
    public ResponseEntity<Void> deleteItemEvents(@RequestBody DeleteEventsDTO dto) {
        eventService.deleteItemAndCommentEvents(dto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/chat/delete")
    public ResponseEntity<Void> deleteChatEvents(@RequestBody DeleteEventsDTO dto) {
        eventService.deleteChatEvents(dto.getId());
        return ResponseEntity.ok().build();
    }

}
