package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.Comment;
import lombok.Builder;

import java.util.UUID;

public class TestComment extends Comment {

    @Builder
    public TestComment(UUID id, UUID parentId, UUID targetId, UUID userId) {
        super();
        super.setId(id);
        super.setParentId(parentId);
        super.setTargetId(targetId);
        super.setUserId(userId);
    }

    public static TestCommentBuilder defaultBuilder() {
        return TestComment.builder()
                .id(UUID.randomUUID())
                .parentId(UUID.randomUUID())
                .targetId(UUID.randomUUID())
                .userId(UUID.randomUUID());
    }

    public Comment toParent() {
        Comment comment = new Comment();
        comment.setId(getId());
        comment.setParentId(getParentId());
        comment.setTargetId(getTargetId());
        comment.setUserId(getUserId());
        return comment;
    }

}
