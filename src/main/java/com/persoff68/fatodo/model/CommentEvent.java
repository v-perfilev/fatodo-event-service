package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.event.Comment;
import com.persoff68.fatodo.model.event.CommentReaction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ftd_event_comment")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"event"}, callSuper = true)
@ToString(exclude = {"event"})
public class CommentEvent extends AbstractModel implements Serializable {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID parentId;

    @NotNull
    private UUID targetId;

    @NotNull
    private UUID commentId;

    @Enumerated(EnumType.STRING)
    private CommentReaction.ReactionType reaction;

    public static CommentEvent of(Comment comment, UUID userId, Event event) {
        CommentEvent commentEvent = new CommentEvent();
        commentEvent.event = event;
        commentEvent.userId = userId;
        commentEvent.parentId = comment.getParentId();
        commentEvent.targetId = commentEvent.getTargetId();
        commentEvent.commentId = commentEvent.getCommentId();
        return commentEvent;
    }

    public static CommentEvent of(CommentReaction reaction, UUID userId, Event event) {
        CommentEvent commentEvent = new CommentEvent();
        commentEvent.event = event;
        commentEvent.userId = userId;
        commentEvent.parentId = reaction.getParentId();
        commentEvent.targetId = reaction.getTargetId();
        commentEvent.commentId = reaction.getCommentId();
        commentEvent.reaction = reaction.getType();
        return commentEvent;
    }

}
