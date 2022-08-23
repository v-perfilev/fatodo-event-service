package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventContactService implements EventService {

    private final EventRepository eventRepository;
    private final EventRecipientRepository eventRecipientRepository;

    public void addEvent(List<UUID> userIdList, EventType type, Object payload) {
        switch (type) {
            case CONTACT_REQUEST_INCOMING -> addContactRequestIncoming(userIdList, type, payload);
            case CONTACT_REQUEST_OUTCOMING -> addContactRequestOutcoming(userIdList, type, payload);
            case CONTACT_ACCEPT_INCOMING -> addContactAcceptIncoming(userIdList, type, payload);
            case CONTACT_ACCEPT_OUTCOMING -> addContactAcceptOutcoming(userIdList, type, payload);
            case CONTACT_DELETE_INCOMING -> addContactDeleteIncoming(userIdList, type, payload);
            case CONTACT_DELETE_OUTCOMING -> addContactDeleteOutcoming(userIdList, type, payload);
            case CONTACT_DELETE -> addContactDelete(userIdList, type, payload);
        }
    }

    private void addContactRequestIncoming(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addContactRequestOutcoming(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addContactAcceptIncoming(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addContactAcceptOutcoming(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addContactDeleteIncoming(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addContactDeleteOutcoming(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addContactDelete(List<UUID> userIdList, EventType type, Object payload) {

    }

}
