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
public class EventCommentService implements EventService {

    private final EventRepository eventRepository;
    private final EventRecipientRepository eventRecipientRepository;

    public void addEvent(List<UUID> userIdList, EventType type, Object payload) {
        switch (type) {
            case COMMENT_CREATE -> addCommentCreate(userIdList, type, payload);
            case COMMENT_REACTION_INCOMING -> addCommentReactionIncoming(userIdList, type, payload);
        }
    }

    private void addCommentCreate(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addCommentReactionIncoming(List<UUID> userIdList, EventType type, Object payload) {

    }

}
