package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.constant.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEventDTO extends AbstractDTO {

    private EventType type;

    private Date date;

    private ContactEventDTO contactEvent;

    private ItemEventDTO itemEvent;

    private CommentEventDTO commentEvent;

    private ChatEventDTO chatEvent;

    private ReminderEventDTO reminderEvent;

}

