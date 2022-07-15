package com.persoff68.fatodo.model;

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
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ftd_event")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Event extends AbstractAuditingModel {

    @Enumerated(EnumType.STRING)
    private EventType type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    private List<EventRecipient> recipients;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    private ContactEvent contactEvent;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    private ItemEvent itemEvent;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    private CommentEvent commentEvent;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    private ChatEvent chatEvent;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    private ReminderEvent reminderEvent;

    public Event(EventType type, List<UUID> recipientIdList) {
        this.type = type;
        this.recipients = recipientIdList.stream().distinct().map(userId -> new EventRecipient(this, userId)).toList();
    }

}
