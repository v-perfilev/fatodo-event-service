package com.persoff68.fatodo.web.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.builder.TestChatEventDTO;
import com.persoff68.fatodo.builder.TestCommentEventDTO;
import com.persoff68.fatodo.builder.TestContactEventDTO;
import com.persoff68.fatodo.builder.TestEventDTO;
import com.persoff68.fatodo.builder.TestItemEventDTO;
import com.persoff68.fatodo.builder.TestReminderEventDTO;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.dto.ChatEventDTO;
import com.persoff68.fatodo.model.dto.CommentEventDTO;
import com.persoff68.fatodo.model.dto.ContactEventDTO;
import com.persoff68.fatodo.model.dto.DeleteChatEventsDTO;
import com.persoff68.fatodo.model.dto.DeleteContactEventsDTO;
import com.persoff68.fatodo.model.dto.DeleteEventsDTO;
import com.persoff68.fatodo.model.dto.DeleteGroupEventsDTO;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.ItemEventDTO;
import com.persoff68.fatodo.model.dto.ReminderEventDTO;
import com.persoff68.fatodo.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {
        "kafka.bootstrapAddress=localhost:9092",
        "kafka.groupId=test",
        "kafka.partitions=1",
        "kafka.autoOffsetResetConfig=earliest"
})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class EventConsumerIT {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private EventConsumer mailConsumer;
    @SpyBean
    private EventService eventService;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeEach
    void setup() {
        kafkaTemplate = buildKafkaTemplate();
    }

    @Test
    void testAddDefaultEvent() throws InterruptedException {
        EventDTO dto = TestEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "default", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addDefaultEvent(any(), any());
    }

    @Test
    void testAddContactEvent() throws InterruptedException {
        ContactEventDTO dto = TestContactEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "contact", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addContactEvent(any(), any(), any());
    }

    @Test
    void testAddItemEvent() throws InterruptedException {
        ItemEventDTO dto = TestItemEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "item", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addItemEvent(any(), any(), any(), any());
    }

    @Test
    void testAddCommentEvent() throws InterruptedException {
        CommentEventDTO dto = TestCommentEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "comment", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addCommentEvent(any(), any(), any());
    }

    @Test
    void testAddChatEvent() throws InterruptedException {
        ChatEventDTO dto = TestChatEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "chat", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addChatEvent(any(), any(), any(), any());
    }

    @Test
    void testAddReminderEvent() throws InterruptedException {
        ReminderEventDTO dto = TestReminderEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "reminder", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addReminderEvent(any(), any(), any());
    }

    @Test
    void testDeleteGroupEventsForUser() throws InterruptedException {
        DeleteGroupEventsDTO dto = new DeleteGroupEventsDTO(UUID.randomUUID(), UUID.randomUUID());
        kafkaTemplate.send("event_delete", "group-delete-user", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).deleteGroupAndCommentEventsForUser(any(), any());
    }

    @Test
    void testDeleteChatEventsForUser() throws InterruptedException {
        DeleteChatEventsDTO dto = new DeleteChatEventsDTO(UUID.randomUUID(), UUID.randomUUID());
        kafkaTemplate.send("event_delete", "chat-delete-user", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).deleteChatEventsForUser(any(), any());
    }

    @Test
    void testDeleteContactEvents() throws InterruptedException {
        DeleteContactEventsDTO dto = new DeleteContactEventsDTO(UUID.randomUUID(), UUID.randomUUID());
        kafkaTemplate.send("event_delete", "contact-delete", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).deleteContactsEvents(any());
    }

    @Test
    void testDeleteGroupEvents() throws InterruptedException {
        DeleteEventsDTO dto = new DeleteEventsDTO(UUID.randomUUID());
        kafkaTemplate.send("event_delete", "group-delete", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).deleteGroupAndCommentEvents(any());
    }

    @Test
    void testDeleteItemEvents() throws InterruptedException {
        DeleteEventsDTO dto = new DeleteEventsDTO(UUID.randomUUID());
        kafkaTemplate.send("event_delete", "item-delete", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).deleteItemAndCommentEvents(any());
    }

    @Test
    void testDeleteChatEvents() throws InterruptedException {
        DeleteEventsDTO dto = new DeleteEventsDTO(UUID.randomUUID());
        kafkaTemplate.send("event_delete", "chat-delete", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).deleteChatEvents(any());
    }


    private <T> KafkaTemplate<String, T> buildKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(embeddedKafkaBroker.getBrokersAsString());
    }

}
