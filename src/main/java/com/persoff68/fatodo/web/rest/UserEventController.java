package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.PageableReadableList;
import com.persoff68.fatodo.model.dto.UserEventDTO;
import com.persoff68.fatodo.model.mapper.EventMapper;
import com.persoff68.fatodo.repository.OffsetPageRequest;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(UserEventController.ENDPOINT)
@RequiredArgsConstructor
public class UserEventController {
    static final String ENDPOINT = "/api/user-events";

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<PageableReadableList<UserEventDTO>> addDefaultEvent(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer size
    ) {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        Pageable pageRequest = OffsetPageRequest.of(offset, size);
        PageableReadableList<Event> eventPageableList = eventService.getAllPageable(userId, pageRequest);
        List<UserEventDTO> dtoList = eventPageableList.getData().stream()
                .map(eventMapper::pojoToDTO)
                .toList();
        PageableReadableList<UserEventDTO> dtoPageableList = PageableReadableList.of(dtoList,
                eventPageableList.getCount(), eventPageableList.getCount());
        return ResponseEntity.ok(dtoPageableList);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> unreadCount() {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        long unreadCount = eventService.getUnreadCount(userId);
        return ResponseEntity.ok(unreadCount);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh() {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        eventService.updateLastRead(userId);
        return ResponseEntity.ok().build();
    }

}
