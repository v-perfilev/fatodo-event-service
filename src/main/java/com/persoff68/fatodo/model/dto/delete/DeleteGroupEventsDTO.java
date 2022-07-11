package com.persoff68.fatodo.model.dto.delete;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteGroupEventsDTO {
    private UUID groupId;

    private UUID userId;
}
