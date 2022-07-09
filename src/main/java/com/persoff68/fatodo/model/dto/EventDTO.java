package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.constant.EventType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class EventDTO {

    @NotNull
    private EventType type;

    private List<UUID> recipientIds;

}
