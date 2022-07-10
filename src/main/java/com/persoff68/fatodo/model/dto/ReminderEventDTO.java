package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReminderEventDTO extends EventDTO {

    @NotNull
    private UUID groupId;

    @NotNull
    private UUID itemId;

}
