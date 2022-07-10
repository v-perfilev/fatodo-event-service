package com.persoff68.fatodo.model.dto;

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
public class ContactEventDTO extends EventDTO {

    @NotNull
    private UUID firstUserId;

    @NotNull
    private UUID secondUserId;

}
