package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemEventDTO extends EventDTO {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID groupId;

    private UUID itemId;

    private List<UUID> userIds;

}
