package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.PageableReadableList;
import com.persoff68.fatodo.model.Status;
import com.persoff68.fatodo.model.constant.StatusType;
import com.persoff68.fatodo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventService {

    private final EventRepository eventRepository;

    @Transactional
    public PageableReadableList<Event> getAllPageable(UUID userId, Pageable pageable) {
        Page<Event> eventPage = eventRepository.findAllByUserId(userId, pageable);
        long unreadCount = eventRepository.countUnreadByUserId(userId);
        return PageableReadableList.of(eventPage.getContent(), eventPage.getTotalElements(), unreadCount);
    }

    public long getUnreadCount(UUID userId) {
        return eventRepository.countUnreadByUserId(userId);
    }

    @Transactional
    public void updateLastRead(UUID userId) {
        List<Event> eventList = eventRepository.getUnreadByUserId(userId);
        eventList.forEach(event -> {
            Status status = Status.of(event, userId, StatusType.READ);
            event.getStatuses().add(status);
        });
        eventRepository.saveAll(eventList);
    }

}
