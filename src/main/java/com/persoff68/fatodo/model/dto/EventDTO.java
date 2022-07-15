package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.constant.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventDTO extends AbstractDTO {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private EventType type;

    private ContactEventDTO contactEvent;

    private ItemEventDTO itemEvent;

    private CommentEventDTO commentEvent;

    private ChatEventDTO chatEvent;

    private ReminderEventDTO reminderEvent;

    private Date createdAt;

}
