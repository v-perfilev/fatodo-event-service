package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.event.Comment;
import com.persoff68.fatodo.model.event.CommentReaction;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.service.exception.EventTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventCommentService implements EventService {

    private final JsonService jsonService;
    private final EventRepository eventRepository;

    public void addEvent(EventDTO eventDTO) {
        switch (eventDTO.getType()) {
            case COMMENT_CREATE -> addCommentEvent(eventDTO);
            case COMMENT_REACTION_INCOMING -> addCommentReactionEvent(eventDTO);
            default -> throw new EventTypeException();
        }
    }

    private void addCommentEvent(EventDTO eventDTO) {
        Comment comment = jsonService.deserialize(eventDTO.getPayload(), Comment.class);
        Event event = new Event(eventDTO);
        CommentEvent commentEvent = CommentEvent.of(comment, eventDTO.getUserId(), event);
        event.setCommentEvent(commentEvent);
        eventRepository.save(event);
    }

    private void addCommentReactionEvent(EventDTO eventDTO) {
        CommentReaction reaction = jsonService.deserialize(eventDTO.getPayload(), CommentReaction.class);
        UUID userId = reaction.getUserId();
        UUID targetId = reaction.getTargetId();
        UUID commentId = reaction.getCommentId();
        eventRepository.deleteCommentReaction(userId, targetId, commentId);
        if (!reaction.getType().equals(CommentReaction.ReactionType.NONE)) {
            Event event = new Event(eventDTO);
            CommentEvent commentEvent = CommentEvent.of(reaction, eventDTO.getUserId(), event);
            event.setCommentEvent(commentEvent);
            eventRepository.save(event);
        }
    }

}
