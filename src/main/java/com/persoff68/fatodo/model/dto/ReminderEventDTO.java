package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ReminderEventDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private UUID groupId;

    private UUID itemId;

}