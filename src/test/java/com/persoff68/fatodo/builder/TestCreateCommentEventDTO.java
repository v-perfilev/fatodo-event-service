package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.create.CreateCommentEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestCreateCommentEventDTO extends CreateCommentEventDTO {

    @Builder
    public TestCreateCommentEventDTO(EventType eventType, UUID userId, UUID parentId, UUID targetId, UUID commentId,
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

    public static TestCreateCommentEventDTOBuilder defaultBuilder() {
        return TestCreateCommentEventDTO.builder()
                .eventType(EventType.COMMENT_ADD)
                .userId(UUID.randomUUID())
                .parentId(UUID.randomUUID())
                .targetId(UUID.randomUUID())
                .commentId(UUID.randomUUID())
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public CreateCommentEventDTO toParent() {
        CreateCommentEventDTO dto = new CreateCommentEventDTO();
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
