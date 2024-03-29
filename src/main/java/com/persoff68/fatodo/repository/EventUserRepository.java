package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, EventUser.EventUserId> {

    @Modifying
    @Query("""
            delete from EventUser r
            where r.userId in :userIds
            and exists (select 1 from ItemEvent i where r.event.id = i.event.id and i.groupId = :groupId)
            """)
    void deleteGroupEventUsers(@Param("groupId") UUID groupId,
                               @Param("userIds") List<UUID> userIds);

    @Modifying
    @Query("""
            delete from EventUser r
            where r.userId in :userIds
            and exists (select 1 from CommentEvent c where r.event.id = c.event.id and c.parentId = :parentId)
            """)
    void deleteCommentEventUsers(@Param("parentId") UUID parentId,
                                 @Param("userIds") List<UUID> userIds);

    @Modifying
    @Query("""
            delete from EventUser r
            where r.userId in :userIds
            and exists (select 1 from ChatEvent c where r.event.id = c.event.id and c.chatId = :chatId)
            """)
    void deleteChatEventUsers(@Param("chatId") UUID chatId,
                              @Param("userIds") List<UUID> userIds);

    @Modifying
    @Query("""
            delete from EventUser r
            where r.userId in :userIds
            and exists (select 1 from ReminderEvent re where r.event.id = re.event.id and re.groupId = :groupId)
            """)
    void deleteReminderEventUsers(@Param("groupId") UUID groupId,
                                  @Param("userIds") List<UUID> userIds);

    @Modifying
    @Query("""
            delete from EventUser r
            where r.userId = :userId
            """)
    void deleteEventUsersByUserId(@Param("userId") UUID userId);

}
