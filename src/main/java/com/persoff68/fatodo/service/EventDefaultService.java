package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventDefaultService implements EventService {

    private final EventRepository eventRepository;

    public void addEvent(EventDTO eventDTO) {
        Event event = new Event(eventDTO);
        eventRepository.save(event);
    }

}
