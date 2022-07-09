package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.EventRecipient;
import com.persoff68.fatodo.model.constant.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRecipientRepository extends JpaRepository<EventRecipient, EventRecipient.EventRecipientId> {

    @Query("""
            delete from EventRecipient r
            where r.userId in :userIds
            and r.event.type in :eventTypes
            and r.event.itemEvent.groupId = :groupId
            """)
    void deleteGroupEventRecipients(@Param("eventTypes") List<EventType> eventTypes,
                                    @Param("groupId") UUID groupId,
                                    @Param("userIds") List<UUID> userIds);

    @Query("""
            delete from EventRecipient r
            where r.userId in :userIds
            and r.event.type in :eventTypes
            and r.event.commentEvent.parentId = :parentId
            """)
    void deleteCommentEventRecipients(@Param("eventTypes") List<EventType> eventTypes,
                                      @Param("parentId") UUID parentId,
                                      @Param("userIds") List<UUID> userIds);

    @Query("""
            delete from EventRecipient r
            where r.userId in :userIds
            and r.event.type in :eventTypes
            and r.event.chatEvent.chatId = :chatId
            """)
    void deleteChatEventRecipients(@Param("eventTypes") List<EventType> eventTypes,
                                   @Param("chatId") UUID targetId,
                                   @Param("userIds") List<UUID> userIds);

}
