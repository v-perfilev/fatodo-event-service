package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.CommentEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestCommentEventDTO extends CommentEventDTO {

    @Builder
    public TestCommentEventDTO(EventType eventType, UUID userId, UUID parentId, UUID targetId, UUID commentId,
                               String reaction, List<UUID> recipientIds) {
        super();
        super.setType(eventType);
        super.setUserId(userId);
        super.setParentId(parentId);
        super.setTargetId(targetId);
        super.setCommentId(commentId);
        super.setReaction(reaction);
        super.setRecipientIds(recipientIds);
    }

    public static TestCommentEventDTOBuilder defaultBuilder() {
        return TestCommentEventDTO.builder()
                .eventType(EventType.COMMENT_ADD)
                .userId(UUID.randomUUID())
                .parentId(UUID.randomUUID())
                .targetId(UUID.randomUUID())
                .commentId(UUID.randomUUID())
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public CommentEventDTO toParent() {
        CommentEventDTO dto = new CommentEventDTO();
        dto.setType(getType());
        dto.setUserId(getUserId());
        dto.setParentId(getParentId());
        dto.setTargetId(getTargetId());
        dto.setCommentId(getCommentId());
        dto.setReaction(getReaction());
        dto.setRecipientIds(getRecipientIds());
        return dto;
    }

}
