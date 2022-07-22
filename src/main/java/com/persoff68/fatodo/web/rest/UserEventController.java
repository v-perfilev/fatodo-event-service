package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.EventMapper;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.PageableReadableList;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.repository.OffsetPageRequest;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.UserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(UserEventController.ENDPOINT)
@RequiredArgsConstructor
public class UserEventController {
    static final String ENDPOINT = "/api/user-event";

    private static final int DEFAULT_SIZE = 30;

    private final UserEventService userEventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<PageableReadableList<EventDTO>> getEventsPageable(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer size
    ) {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        offset = Optional.ofNullable(offset).orElse(0);
        size = Optional.ofNullable(size).orElse(DEFAULT_SIZE);
        Pageable pageRequest = OffsetPageRequest.of(offset, size);
        PageableReadableList<Event> eventPageableList = userEventService.getAllPageable(userId, pageRequest);
        List<EventDTO> dtoList = eventPageableList.getData().stream()
                .map(eventMapper::pojoToDTO)
                .toList();
        PageableReadableList<EventDTO> dtoPageableList = PageableReadableList.of(dtoList,
                eventPageableList.getCount(), eventPageableList.getCount());
        return ResponseEntity.ok(dtoPageableList);
    }

    @GetMapping("/unread")
    public ResponseEntity<Long> getUnreadCount() {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        long unreadCount = userEventService.getUnreadCount(userId);
        return ResponseEntity.ok(unreadCount);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh() {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        userEventService.updateLastRead(userId);
        return ResponseEntity.ok().build();
    }

}
