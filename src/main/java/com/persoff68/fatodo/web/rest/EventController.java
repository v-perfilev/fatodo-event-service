package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.dto.ChatEventDTO;
import com.persoff68.fatodo.model.dto.CommentEventDTO;
import com.persoff68.fatodo.model.dto.ContactEventDTO;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.ItemEventDTO;
import com.persoff68.fatodo.model.mapper.EventMapper;
import com.persoff68.fatodo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/contact/{firstUserId}/{secondUserId}")
    public ResponseEntity<Void> deleteContactEventsForUsers(@PathVariable UUID firstUserId,
                                                            @PathVariable UUID secondUserId) {
        List<UUID> userIdList = List.of(firstUserId, secondUserId);
        eventService.deleteContactsEventForUsers(userIdList);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/group/{groupId}/{userId}")
    public ResponseEntity<Void> deleteGroupAndCommentEventsForUser(@PathVariable UUID groupId,
                                                                   @PathVariable UUID userId) {
        List<UUID> userIdList = List.of(userId);
        eventService.deleteGroupAndCommentEventsForUsers(groupId, userIdList);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/chat/{chatId}/{userId}")
    public ResponseEntity<Void> deleteChatEventsForUsers(@PathVariable UUID chatId,
                                                        @PathVariable UUID userId) {
        List<UUID> userIdList = List.of(userId);
        eventService.deleteChatEventsForUsers(chatId, userIdList);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<Void> deleteGroupEvents(@PathVariable UUID groupId) {
        eventService.deleteGroupAndCommentEvents(groupId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Void> deleteItemEvents(@PathVariable UUID itemId) {
        eventService.deleteItemAndCommentEvents(itemId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/chat/{chatId}")
    public ResponseEntity<Void> deleteChatEvents(@PathVariable UUID chatId) {
        eventService.deleteChatEvents(chatId);
        return ResponseEntity.ok().build();
    }

}
