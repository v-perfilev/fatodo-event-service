package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.event.ContactRequest;
import com.persoff68.fatodo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventContactService implements EventService {

    private final JsonService jsonService;
    private final EventRepository eventRepository;

    public void addEvent(EventDTO eventDTO) {
        switch (eventDTO.getType()) {
            case CONTACT_REQUEST_INCOMING, CONTACT_REQUEST_OUTCOMING -> addRequestEvent(eventDTO);
            case CONTACT_ACCEPT_INCOMING, CONTACT_ACCEPT_OUTCOMING -> addContactAcceptEvent(eventDTO);
            case CONTACT_DELETE_INCOMING, CONTACT_DELETE_OUTCOMING -> addContactDeleteEvent(eventDTO);
            case CONTACT_DELETE -> deleteContactEvents(eventDTO);
        }
    }

    private void addRequestEvent(EventDTO eventDTO) {
        ContactRequest contactRequest = jsonService.deserialize(eventDTO.getPayload(), ContactRequest.class);
        Event event = new Event(eventDTO);
        ContactEvent contactEvent = ContactEvent.of(contactRequest, eventDTO.getUserId(), event);
        event.setContactEvent(contactEvent);
        eventRepository.save(event);
    }

    private void addContactAcceptEvent(EventDTO eventDTO) {
        ContactRequest contactRequest = jsonService.deserialize(eventDTO.getPayload(), ContactRequest.class);
        Event event = new Event(eventDTO);
        ContactEvent contactEvent = ContactEvent.of(contactRequest, eventDTO.getUserId(), event);
        event.setContactEvent(contactEvent);
        eventRepository.deleteContactEvents(eventDTO.getUserIds());
        eventRepository.save(event);
    }

    private void addContactDeleteEvent(EventDTO eventDTO) {
        ContactRequest contactRequest = jsonService.deserialize(eventDTO.getPayload(), ContactRequest.class);
        Event event = new Event(eventDTO);
        ContactEvent contactEvent = ContactEvent.of(contactRequest, eventDTO.getUserId(), event);
        event.setContactEvent(contactEvent);
        eventRepository.deleteContactEvents(eventDTO.getUserIds());
        eventRepository.save(event);
    }

    private void deleteContactEvents(EventDTO eventDTO) {
        eventRepository.deleteContactEvents(eventDTO.getUserIds());
    }

}
