package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("""
            select e from Event e
            join EventUser u on e.id = u.event.id
            where u.userId = :userId
            order by e.createdAt desc
            """)
    Page<Event> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
            select count(e) from Event e
            join EventUser u on e.id = u.event.id
            where u.userId = :userId and e.createdAt > :from
            """)
    long countFromByUserId(@Param("userId") UUID userId, @Param("from") Date from);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from ContactEvent c where e.id = c.event.id
            and c.firstUserId in :userIds and c.secondUserId in :userIds)
            """)
    void deleteContactEvents(@Param("userIds") List<UUID> userIds);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from ItemEvent i where e.id = i.event.id and i.groupId = :groupId)
            and e.users.size = 0
            """)
    void deleteEmptyItemGroupEvents(@Param("groupId") UUID groupId);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from CommentEvent c where e.id = c.event.id and c.parentId = :parentId)
            and e.users.size = 0
            """)
    void deleteEmptyCommentEvents(@Param("parentId") UUID parentId);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from ChatEvent c where e.id = c.event.id and c.chatId = :chatId)
            and e.users.size = 0
            """)
    void deleteEmptyChatEvents(@Param("chatId") UUID chatId);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from ReminderEvent r where e.id = r.event.id and r.groupId = :groupId)
            and e.users.size = 0
            """)
    void deleteEmptyReminderEvents(@Param("groupId") UUID groupId);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from ItemEvent i where e.id = i.event.id and i.groupId = :groupId)
            """)
    void deleteGroupEvents(@Param("groupId") UUID groupId);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from ItemEvent i where e.id = i.event.id and i.itemId = :itemId)
            """)
    void deleteItemEvents(@Param("itemId") UUID itemId);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from CommentEvent c where e.id = c.event.id and c.parentId = :parentId)
            """)
    void deleteCommentEventsByParentId(@Param("parentId") UUID parentId);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from CommentEvent c where e.id = c.event.id and c.targetId = :targetId)
            """)
    void deleteCommentEventsByTargetId(@Param("targetId") UUID targetId);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from ChatEvent c where e.id = c.event.id and c.chatId = :chatId)
            """)
    void deleteChatEvents(@Param("chatId") UUID chatId);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from ReminderEvent r where e.id = r.event.id and r.groupId = :groupId)
            """)
    void deleteReminderEventsByGroupId(@Param("groupId") UUID groupId);

    @Modifying
    @Query("""
            delete from Event e
            where exists (select 1 from ReminderEvent r where e.id = r.event.id and r.itemId = :itemId)
            """)
    void deleteReminderEventsByItemId(@Param("itemId") UUID itemId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in com.persoff68.fatodo.model.constant.EventType.CHAT_REACTION_INCOMING
            and exists (select 1 from ChatEvent c where e.id = c.event.id
                and c.userId = :userId and c.chatId = :chatId and c.messageId = :messageId)
            and e.users.size = 0
            """)
    void deleteChatReaction(@Param("userId") UUID userId, @Param("chatId") UUID chatId,
                            @Param("messageId") UUID messageId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in com.persoff68.fatodo.model.constant.EventType.COMMENT_REACTION_INCOMING
            and exists (select 1 from CommentEvent c where e.id = c.event.id
                and c.userId = :userId and c.targetId = :targetId and c.commentId = :commentId)
            and e.users.size = 0
            """)
    void deleteCommentReaction(@Param("userId") UUID userId, @Param("targetId") UUID targetId,
                               @Param("commentId") UUID commentId);

}
