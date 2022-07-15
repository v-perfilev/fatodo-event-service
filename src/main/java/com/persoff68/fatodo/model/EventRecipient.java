package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ftd_event_recipient")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(EventRecipient.EventRecipientId.class)
@ToString(exclude = {"event"})
public class EventRecipient implements Serializable {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @Id
    @ManyToOne
    private Event event;

    @Id
    private UUID userId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventRecipientId implements Serializable {
        @Serial
        private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

        private Event event;
        private UUID userId;
    }

}
