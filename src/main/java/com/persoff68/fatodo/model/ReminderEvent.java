package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.event.Reminder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ftd_event_reminder")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"event"}, callSuper = true)
@ToString(exclude = {"event"})
public class ReminderEvent extends AbstractModel implements Serializable {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    private UUID groupId;

    @NotNull
    private UUID itemId;

    public ReminderEvent(Event event, ReminderEvent reminderEvent) {
        this.event = event;
        this.groupId = reminderEvent.groupId;
        this.itemId = reminderEvent.itemId;
    }

    public static ReminderEvent of(Reminder reminder, Event event){
        ReminderEvent reminderEvent = new ReminderEvent();
        reminderEvent.event = event;
        reminderEvent.groupId = reminder.getParentId();
        reminderEvent.itemId = reminder.getTargetId();
        return reminderEvent;
    }

}
