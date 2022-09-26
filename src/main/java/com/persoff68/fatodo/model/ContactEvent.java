package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
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
@Table(name = "ftd_event_contact")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"event"}, callSuper = true)
@ToString(exclude = {"event"})
public class ContactEvent extends AbstractModel implements Serializable {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID firstUserId;

    @NotNull
    private UUID secondUserId;

    public static ContactEvent of(UUID firstUserId, UUID secondUserId, UUID userId, Event event) {
        ContactEvent contactEvent = new ContactEvent();
        contactEvent.event = event;
        contactEvent.userId = userId;
        contactEvent.firstUserId = firstUserId;
        contactEvent.secondUserId = secondUserId;
        return contactEvent;
    }

}
