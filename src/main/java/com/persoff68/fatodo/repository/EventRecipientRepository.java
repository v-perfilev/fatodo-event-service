package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.EventRecipient;
import com.persoff68.fatodo.model.constant.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRecipientRepository extends JpaRepository<EventRecipient, EventRecipient.EventRecipientId> {

    @Modifying
    @Query("""
            delete from EventRecipient r
            where r.userId in :userIds
            and r in (select er from EventRecipient er
                join Event e on er.event.id = e.id where e.type in :eventTypes)
            and r in (select er from EventRecipient er
                join ItemEvent ie on er.event.id = ie.event.id where ie.groupId = :groupId)
            """)
    void deleteGroupEventRecipients(@Param("eventTypes") List<EventType> eventTypes,
                                    @Param("groupId") UUID groupId,
                                    @Param("userIds") List<UUID> userIds);

    @Modifying
    @Query("""
            delete from EventRecipient r
            where r.userId in :userIds
            and r in (select er from EventRecipient er
                join Event e on er.event.id = e.id where e.type in :eventTypes)
            and r in (select er from EventRecipient er
                join CommentEvent ce on er.event.id = ce.event.id where ce.parentId = :parentId)
            """)
    void deleteCommentEventRecipients(@Param("eventTypes") List<EventType> eventTypes,
                                      @Param("parentId") UUID parentId,
                                      @Param("userIds") List<UUID> userIds);

    @Modifying
    @Query("""
            delete from EventRecipient r
            where r.userId in :userIds
            and r in (select er from EventRecipient er
                join Event e on er.event.id = e.id where e.type in :eventTypes)
            and r in (select er from EventRecipient er
                join ChatEvent ce on er.event.id = ce.event.id where ce.chatId = :chatId)
            """)
    void deleteChatEventRecipients(@Param("eventTypes") List<EventType> eventTypes,
                                   @Param("chatId") UUID chatId,
                                   @Param("userIds") List<UUID> userIds);

    @Modifying
    @Query("""
            delete from EventRecipient r
            where r.userId in :userIds
            and r in (select er from EventRecipient er
                join Event e on er.event.id = e.id where e.type in :eventTypes)
            and r in (select er from EventRecipient er
                join ReminderEvent re on er.event.id = re.event.id where re.groupId = :groupId)
            """)
    void deleteReminderEventRecipients(@Param("eventTypes") List<EventType> eventTypes,
                                       @Param("groupId") UUID groupId,
                                       @Param("userIds") List<UUID> userIds);

}
