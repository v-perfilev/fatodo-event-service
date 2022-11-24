package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.ReminderEvent;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.event.ReminderMeta;
import com.persoff68.fatodo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventReminderService implements EventService {

    private final JsonService jsonService;
    private final EventRepository eventRepository;

    public void addEvent(EventDTO eventDTO) {
        ReminderMeta reminderMeta = jsonService.deserialize(eventDTO.getPayload(), ReminderMeta.class);
        Event event = new Event(eventDTO);
        ReminderEvent reminderEvent = ReminderEvent.of(reminderMeta, event);
        event.setReminderEvent(reminderEvent);
        eventRepository.save(event);
    }

}
