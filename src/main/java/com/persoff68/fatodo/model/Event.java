package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "ftd_event")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"statuses"})
public class Event extends AbstractModel implements Serializable {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private UUID userId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private List<EventUser> users;

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

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "event", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Status> statuses = new HashSet<>();

    public Event(EventDTO eventDTO) {
        this.type = eventDTO.getType();
        this.userId = eventDTO.getUserId();
        this.users = eventDTO.getUserIds().stream().distinct()
                .map(userId -> new EventUser(this, userId))
                .toList();
        this.date = eventDTO.getDate();
    }

}
