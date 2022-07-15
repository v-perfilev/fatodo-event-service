package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ContactEventDTO {

    private UUID firstUserId;
    private UUID secondUserId;

}
