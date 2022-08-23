package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventDefaultService implements EventService {

    private final EventRepository eventRepository;

    public void addEvent(List<UUID> userIdList, EventType type, Object payload) {
        Event event = new Event(type, userIdList);
        eventRepository.save(event);
    }

}
