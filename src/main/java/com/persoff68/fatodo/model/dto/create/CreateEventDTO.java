package com.persoff68.fatodo.model.dto.create;

import com.persoff68.fatodo.model.constant.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO implements Serializable {

    @NotNull
    private EventType type;

    private List<UUID> recipientIds;

}
