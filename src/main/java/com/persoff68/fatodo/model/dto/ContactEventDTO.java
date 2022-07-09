package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContactEventDTO extends EventDTO {

    @NotNull
    private UUID firstUserId;

    @NotNull
    private UUID secondUserId;

    private Set<UUID> userIds;

}
