package com.persoff68.fatodo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "ftd_event_read_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatus {

    @Id
    private UUID userId;

    private Date lastReadAt;

}
