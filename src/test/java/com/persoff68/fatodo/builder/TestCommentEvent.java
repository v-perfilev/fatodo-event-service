package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.CommentEvent;
import lombok.Builder;

import java.util.UUID;

public class TestCommentEvent extends CommentEvent {

    @Builder
    public TestCommentEvent(UUID id, UUID userId, UUID parentId, UUID targetId, UUID commentId, String reaction) {
        super();
        super.setId(id);
        super.setUserId(userId);
        super.setParentId(parentId);
        super.setTargetId(targetId);
        super.setCommentId(commentId);
        super.setReaction(reaction);
    }

    public static TestCommentEventBuilder defaultBuilder() {
        return TestCommentEvent.builder()
                .userId(UUID.randomUUID())
                .parentId(UUID.randomUUID())
                .targetId(UUID.randomUUID())
                .commentId(UUID.randomUUID());
    }

    public CommentEvent toParent() {
        CommentEvent commentEvent = new CommentEvent();
        commentEvent.setId(getId());
        commentEvent.setUserId(getUserId());
        commentEvent.setParentId(getParentId());
        commentEvent.setTargetId(getTargetId());
        commentEvent.setCommentId(getCommentId());
        commentEvent.setReaction(getReaction());
        return commentEvent;
    }

}
