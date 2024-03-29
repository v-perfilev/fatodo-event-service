package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.EventMapper;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.PageableReadableList;
import com.persoff68.fatodo.model.dto.UserEventDTO;
import com.persoff68.fatodo.repository.OffsetPageRequest;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.UserEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(UserEventController.ENDPOINT)
@RequiredArgsConstructor
@Slf4j
public class UserEventController {
    static final String ENDPOINT = "/api/user-event";

    private static final int DEFAULT_SIZE = 30;

    private final UserEventService userEventService;
    private final EventMapper eventMapper;

    @GetMapping
    @Transactional
    public ResponseEntity<PageableReadableList<UserEventDTO>> getEventsPageable(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer size
    ) {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        offset = Optional.ofNullable(offset).orElse(0);
        size = Optional.ofNullable(size).orElse(DEFAULT_SIZE);
        Pageable pageRequest = OffsetPageRequest.of(offset, size);
        PageableReadableList<Event> eventList = userEventService.getAllPageable(userId, pageRequest);
        PageableReadableList<UserEventDTO> dtoList = eventList.convert(eventMapper::pojoToDTO);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/unread")
    public ResponseEntity<Long> getUnreadCount() {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        long unreadCount = userEventService.getUnreadCount(userId);
        return ResponseEntity.ok(unreadCount);
    }

    @PutMapping("/refresh")
    public ResponseEntity<Void> refresh() {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        userEventService.updateLastRead(userId);
        return ResponseEntity.ok().build();
    }

}
