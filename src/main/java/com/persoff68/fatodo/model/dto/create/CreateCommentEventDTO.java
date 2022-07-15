package com.persoff68.fatodo.model.dto.create;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateCommentEventDTO extends CreateEventDTO {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID parentId;

    @NotNull
    private UUID targetId;

    @NotNull
    private UUID commentId;

    private String reaction;

}
