package com.persoff68.fatodo.model.dto.delete;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteContactEventsDTO {

    @NotNull
    private UUID firstUserId;

    @NotNull
    private UUID secondUserId;

}
