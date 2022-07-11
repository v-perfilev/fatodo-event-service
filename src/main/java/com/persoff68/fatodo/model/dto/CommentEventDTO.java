package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CommentEventDTO {

    private UUID userId;

    private UUID parentId;

    private UUID targetId;

    private UUID commentId;

    private String reaction;

}