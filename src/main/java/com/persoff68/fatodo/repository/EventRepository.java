package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.constant.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
            join EventRecipient r
            where r.userId = : userId
            order by e.createdAt desc
            """)
    Page<Event> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
            select count(e) from Event e
            join EventRecipient r
            where r.userId = : userId and e.createdAt > :from
            """)
    long countFromByUserId(@Param("from") UUID userId, @Param("from") Date from);

    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e.contactEvent.firstUserId in :userId
            and e.contactEvent.secondUserId in :userIds
            """)
    void deleteContactEvent(@Param("eventTypes") List<EventType> eventTypes, @Param("userIds") List<UUID> userIds);

    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e.itemEvent.groupId = :groupId
            and e.recipients.size = 0
            """)
    void deleteEmptyItemGroupEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("groupId") UUID groupId);

    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e.commentEvent.parentId = :parentId
            and e.recipients.size = 0
            """)
    void deleteEmptyCommentEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("parentId") UUID parentId);

    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e.chatEvent.chatId = :chatId
            and e.recipients.size = 0
            """)
    void deleteEmptyChatEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("chatId") UUID chatId);

    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e.itemEvent.groupId = :groupId
            """)
    void deleteGroupEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("groupId") UUID groupId);

    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e.itemEvent.itemId = :itemId
            """)
    void deleteItemEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("itemId") UUID itemId);

    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e.commentEvent.parentId = :parentId
            """)
    void deleteCommentEventsByParentId(@Param("eventTypes") List<EventType> eventTypes,
                                       @Param("parentId") UUID parentId);

    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e.commentEvent.targetId = :targetId
            """)
    void deleteCommentEventsByTargetId(@Param("eventTypes") List<EventType> eventTypes,
                                       @Param("targetId") UUID targetId);

    @Query("""
            delete from Event e
            where e.type in :eventTypes
            and e.chatEvent.chatId = :chatId
            """)
    void deleteChatEvents(@Param("eventTypes") List<EventType> eventTypes, @Param("chatId") UUID chatId);

}
