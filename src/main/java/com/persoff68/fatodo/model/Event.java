package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.constant.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ftd_event")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Event extends AbstractAuditingModel implements Serializable {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private List<EventRecipient> recipients;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "event")
    private ContactEvent contactEvent;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "event")
    private ItemEvent itemEvent;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "event")
    private CommentEvent commentEvent;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "event")
    private ChatEvent chatEvent;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "event")
    private ReminderEvent reminderEvent;

    public Event(EventType type, List<UUID> recipientIdList) {
        this.type = type;
        this.recipients = recipientIdList != null
                ? recipientIdList.stream().distinct().map(userId -> new EventRecipient(this, userId)).toList()
                : Collections.emptyList();
    }

}
