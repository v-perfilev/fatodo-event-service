package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.event.ContactRequest;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.service.exception.EventTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventContactService implements EventService {

    private final JsonService jsonService;
    private final EventRepository eventRepository;

    public void addEvent(EventDTO eventDTO) {
        switch (eventDTO.getType()) {
            case CONTACT_REQUEST_INCOMING, CONTACT_REQUEST_OUTCOMING -> addContactRequestEvent(eventDTO);
            case CONTACT_ACCEPT_INCOMING, CONTACT_ACCEPT_OUTCOMING -> addContactRequestAcceptEvent(eventDTO);
            case CONTACT_DELETE_INCOMING, CONTACT_DELETE_OUTCOMING -> addContactRequestDeleteEvent(eventDTO);
            case CONTACT_DELETE -> deleteContactRelationEvents(eventDTO);
            default -> throw new EventTypeException();
        }
    }

    private void addContactRequestEvent(EventDTO eventDTO) {
        ContactRequest contactRequest = jsonService.deserialize(eventDTO.getPayload(), ContactRequest.class);
        Event event = new Event(eventDTO);
        ContactEvent contactEvent = ContactEvent.of(
                contactRequest.getRequesterId(),
                contactRequest.getRecipientId(),
                eventDTO.getUserId(),
                event);
        event.setContactEvent(contactEvent);
        eventRepository.save(event);
    }

    private void addContactRequestAcceptEvent(EventDTO eventDTO) {
        ContactRequest contactRequest = jsonService.deserialize(eventDTO.getPayload(), ContactRequest.class);
        Event event = new Event(eventDTO);
        ContactEvent contactEvent = ContactEvent.of(
                contactRequest.getRecipientId(),
                contactRequest.getRequesterId(),
                eventDTO.getUserId(),
                event);
        event.setContactEvent(contactEvent);
        List<UUID> userIdList = List.of(contactRequest.getRequesterId(), contactRequest.getRecipientId());
        eventRepository.deleteContactEvents(userIdList);
        eventRepository.save(event);
    }

    private void addContactRequestDeleteEvent(EventDTO eventDTO) {
        ContactRequest contactRequest = jsonService.deserialize(eventDTO.getPayload(), ContactRequest.class);
        Event event = new Event(eventDTO);
        ContactEvent contactEvent = ContactEvent.of(
                contactRequest.getRequesterId(),
                contactRequest.getRecipientId(),
                eventDTO.getUserId(),
                event);
        event.setContactEvent(contactEvent);
        List<UUID> userIdList = List.of(contactRequest.getRequesterId(), contactRequest.getRecipientId());
        eventRepository.deleteContactEvents(userIdList);
        eventRepository.save(event);
    }

    private void deleteContactRelationEvents(EventDTO eventDTO) {
        eventRepository.deleteContactEvents(eventDTO.getUserIds());
    }

}
