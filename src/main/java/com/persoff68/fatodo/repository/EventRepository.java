package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.constant.EventType;
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
            join EventRecipient r on e.id = r.event.id
            where r.userId = :userId
            order by e.createdAt desc
            """)
    Page<Event> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
            select count(e) from Event e
            join EventRecipient r on e.id = r.event.id
            where r.userId = :userId and e.createdAt > :from
            """)
    long countFromByUserId(@Param("userId") UUID userId, @Param("from") Date from);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join ContactEvent c on ev.id = c.event.id where c.firstUserId in :userIds and c.secondUserId in :userIds)
            """)
    void deleteContactEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("userIds") List<UUID> userIds);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join ItemEvent i on ev.id = i.event.id where i.groupId = :groupId)
            and e.recipients.size = 0
            """)
    void deleteEmptyItemGroupEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("groupId") UUID groupId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join CommentEvent c on ev.id = c.event.id where c.parentId = :parentId)
            and e.recipients.size = 0
            """)
    void deleteEmptyCommentEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("parentId") UUID parentId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join ChatEvent c on ev.id = c.event.id where c.chatId = :chatId)
            and e.recipients.size = 0
            """)
    void deleteEmptyChatEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("chatId") UUID chatId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join ReminderEvent r on ev.id = r.event.id where r.groupId = :groupId)
            and e.recipients.size = 0
            """)
    void deleteEmptyReminderEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("groupId") UUID groupId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join ItemEvent i on ev.id = i.event.id where i.groupId = :groupId)
            """)
    void deleteGroupEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("groupId") UUID groupId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join ItemEvent i on ev.id = i.event.id where i.itemId = :itemId)
            """)
    void deleteItemEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("itemId") UUID itemId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join CommentEvent c on ev.id = c.event.id where c.parentId = :parentId)
            """)
    void deleteCommentEventsByParentId(@Param("eventTypes") List<EventType> eventTypes,
                                       @Param("parentId") UUID parentId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join CommentEvent c on ev.id = c.event.id where c.targetId = :targetId)
            """)
    void deleteCommentEventsByTargetId(@Param("eventTypes") List<EventType> eventTypes,
                                       @Param("targetId") UUID targetId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join ChatEvent c on ev.id = c.event.id where c.chatId = :chatId)
            """)
    void deleteChatEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("chatId") UUID chatId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join ReminderEvent r on ev.id = r.event.id where r.groupId = :groupId)
            """)
    void deleteReminderEventsByGroupId(@Param("eventTypes") List<EventType> eventTypes, @Param("groupId") UUID groupId);

    @Modifying
    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e in (select ev from Event ev join ReminderEvent r on ev.id = r.event.id where r.itemId = :itemId)
            """)
    void deleteReminderEventsByItemId(@Param("eventTypes") List<EventType> eventTypes, @Param("itemId") UUID itemId);

}
