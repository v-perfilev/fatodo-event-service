package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.CommentReaction;
import lombok.Builder;

import java.util.UUID;

public class TestCommentReaction extends CommentReaction {

    @Builder
    public TestCommentReaction(UUID parentId, UUID targetId, UUID userId, ReactionType type) {
        super();
        super.setParentId(parentId);
        super.setTargetId(targetId);
        super.setUserId(userId);
        super.setType(type);
    }

    public static TestCommentReactionBuilder defaultBuilder() {
        return TestCommentReaction.builder()
                .parentId(UUID.randomUUID())
                .targetId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .type(ReactionType.LIKE);
    }

    public CommentReaction toParent() {
        CommentReaction commentReaction = new CommentReaction();
        commentReaction.setParentId(getParentId());
        commentReaction.setTargetId(getTargetId());
        commentReaction.setUserId(getUserId());
        commentReaction.setType(getType());
        return commentReaction;
    }

}
