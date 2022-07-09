package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChatEventDTO extends EventDTO {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID chatId;

    private UUID messageId;

    private String reaction;

    private List<UUID> userIds;

}
