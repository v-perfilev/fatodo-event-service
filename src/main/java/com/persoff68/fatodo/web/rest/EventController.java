package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.EventMapper;
import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.ReminderEvent;
import com.persoff68.fatodo.model.dto.create.CreateChatEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateCommentEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateItemEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateReminderEventDTO;
import com.persoff68.fatodo.model.dto.delete.DeleteContactEventsDTO;
import com.persoff68.fatodo.model.dto.delete.DeleteEventsDTO;
import com.persoff68.fatodo.model.dto.delete.DeleteUserEventsDTO;
import com.persoff68.fatodo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(EventController.ENDPOINT)
@RequiredArgsConstructor
public class EventController {
    static final String ENDPOINT = "/api/event";

    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping("/default")
    public ResponseEntity<Void> addDefaultEvent(@Valid @RequestBody CreateEventDTO dto) {
        eventService.addDefaultEvent(dto.getType(), dto.getRecipientIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/contact")
    public ResponseEntity<Void> addContactEvent(@Valid @RequestBody CreateContactEventDTO dto) {
        ContactEvent contactEvent = eventMapper.contactDTOToPojo(dto);
        eventService.addContactEvent(dto.getType(), dto.getRecipientIds(), contactEvent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/item")
    public ResponseEntity<Void> addItemEvent(@Valid @RequestBody CreateItemEventDTO dto) {
        ItemEvent itemEvent = eventMapper.itemDTOToPojo(dto);
        eventService.addItemEvent(dto.getType(), dto.getRecipientIds(), itemEvent, dto.getUserIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comment")
    public ResponseEntity<Void> addCommentEvent(@Valid @RequestBody CreateCommentEventDTO dto) {
        CommentEvent commentEvent = eventMapper.commentDTOToPojo(dto);
        eventService.addCommentEvent(dto.getType(), dto.getRecipientIds(), commentEvent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/chat")
    public ResponseEntity<Void> addChatEvent(@Valid @RequestBody CreateChatEventDTO dto) {
        ChatEvent chatEvent = eventMapper.chatDTOToPojo(dto);
        eventService.addChatEvent(dto.getType(), dto.getRecipientIds(), chatEvent, dto.getUserIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/reminder")
    public ResponseEntity<Void> addReminderEvent(@Valid @RequestBody CreateReminderEventDTO dto) {
        ReminderEvent reminderEvent = eventMapper.reminderDTOToPojo(dto);
        eventService.addReminderEvent(dto.getType(), dto.getRecipientIds(), reminderEvent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/group/delete-users")
    public ResponseEntity<Void> deleteGroupEventsForUsers(@Valid @RequestBody DeleteUserEventsDTO dto) {
        eventService.deleteGroupEventsForUser(dto.getId(), dto.getUserIds());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/chat/delete-users")
    public ResponseEntity<Void> deleteChatEventsForUsers(@Valid @RequestBody DeleteUserEventsDTO dto) {
        eventService.deleteChatEventsForUser(dto.getId(), dto.getUserIds());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/contact/delete")
    public ResponseEntity<Void> deleteContactEvents(@Valid @RequestBody DeleteContactEventsDTO dto) {
        List<UUID> userIdList = List.of(dto.getFirstUserId(), dto.getSecondUserId());
        eventService.deleteContactsEvents(userIdList);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/group/delete")
    public ResponseEntity<Void> deleteGroupEvents(@Valid @RequestBody DeleteEventsDTO dto) {
        eventService.deleteGroupEvents(dto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/item/delete")
    public ResponseEntity<Void> deleteItemEvents(@Valid @RequestBody DeleteEventsDTO dto) {
        eventService.deleteItemEvents(dto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/chat/delete")
    public ResponseEntity<Void> deleteChatEvents(@Valid @RequestBody DeleteEventsDTO dto) {
        eventService.deleteChatEvents(dto.getId());
        return ResponseEntity.ok().build();
    }

}
