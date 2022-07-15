package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatEventDTO {

    private UUID userId;

    private UUID chatId;

    private UUID messageId;

    private String reaction;

    private List<UUID> userIds;

}
