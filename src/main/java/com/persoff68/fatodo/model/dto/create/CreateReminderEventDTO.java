package com.persoff68.fatodo.model.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateReminderEventDTO extends CreateEventDTO {

    @NotNull
    private UUID groupId;

    @NotNull
    private UUID itemId;

}
