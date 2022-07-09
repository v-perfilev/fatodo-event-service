package com.persoff68.fatodo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ftd_event_item_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ItemEventUser.ItemEventUserId.class)
@ToString(exclude = {"itemEvent"})
public class ItemEventUser {

    @Id
    @ManyToOne
    private ItemEvent itemEvent;

    @Id
    private UUID userId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ItemEventUserId implements Serializable {
        private ItemEvent itemEvent;
        private UUID userId;
    }

}
