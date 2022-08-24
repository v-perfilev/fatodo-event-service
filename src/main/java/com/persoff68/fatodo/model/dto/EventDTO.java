package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.constant.EventType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class EventDTO {

    @NotNull
    @NotEmpty
    private List<UUID> userIds;

    @NotNull
    private EventType type;

    private String payload;

    private UUID userId;

    @NotNull
    private Date date;

}
