package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.service.exception.ModelInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventChatService implements EventService {

    private final EventRepository eventRepository;
    private final EventRecipientRepository eventRecipientRepository;

    public void addEvent(List<UUID> userIdList, EventType type, Object payload) {
        switch (type) {
            case CHAT_CREATE -> addChatCreate(userIdList, type, payload);
            case CHAT_UPDATE -> addChatUpdate(userIdList, type, payload);
            case CHAT_DELETE -> addChatDelete(userIdList, type, payload);
            case CHAT_MEMBER_ADD -> addChatMemberAdd(userIdList, type, payload);
            case CHAT_MEMBER_DELETE -> addChatMemberDelete(userIdList, type, payload);
            case CHAT_MEMBER_LEAVE -> addChatMemberLeave(userIdList, type, payload);
            case CHAT_REACTION_INCOMING -> addChatReactionIncoming(userIdList, type, payload);
        }
    }

    public void addChatCreate(List<UUID> userIdList, EventType type, Object payload) {

    }

    public void addChatUpdate(List<UUID> userIdList, EventType type, Object payload) {

    }

    public void addChatDelete(List<UUID> userIdList, EventType type, Object payload) {

    }

    public void addChatMemberAdd(List<UUID> userIdList, EventType type, Object payload) {

    }

    public void addChatMemberDelete(List<UUID> userIdList, EventType type, Object payload) {

    }

    public void addChatMemberLeave(List<UUID> userIdList, EventType type, Object payload) {

    }

    public void addChatReactionIncoming(List<UUID> userIdList, EventType type, Object payload) {

    }

}
