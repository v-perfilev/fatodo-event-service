package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentEventDTO extends EventDTO {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID parentId;

    @NotNull
    private UUID targetId;

    @NotNull
    private UUID commentId;

    private String reaction;

}
