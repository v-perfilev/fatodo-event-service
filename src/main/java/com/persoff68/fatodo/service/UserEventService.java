package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.PageableReadableList;
import com.persoff68.fatodo.model.ReadStatus;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserEventService {

    private final EventRepository eventRepository;
    private final ReadStatusRepository readStatusRepository;

    @Transactional
    public PageableReadableList<Event> getAllPageable(UUID userId, Pageable pageable) {
        Date lastReadAt = updateLastRead(userId);
        Page<Event> eventPage = eventRepository.findAllByUserId(userId, pageable);
        long unreadCount = eventRepository.countFromByUserId(userId, lastReadAt);
        return PageableReadableList.of(eventPage.getContent(), eventPage.getTotalElements(), unreadCount);
    }

    public long getUnreadCount(UUID userId) {
        ReadStatus readStatus = readStatusRepository.findByUserId(userId)
                .orElse(new ReadStatus(userId, new Date(0)));
        Date from = readStatus.getLastReadAt();
        return eventRepository.countFromByUserId(userId, from);
    }

    @Transactional
    public Date updateLastRead(UUID userId) {
        ReadStatus readStatus = readStatusRepository.findByUserId(userId)
                .orElse(new ReadStatus(userId, new Date(0)));
        Date from = readStatus.getLastReadAt();
        readStatus.setLastReadAt(new Date());
        readStatusRepository.save(readStatus);
        return from;
    }

}
