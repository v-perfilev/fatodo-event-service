package com.persoff68.fatodo.web.kafka;

import com.persoff68.fatodo.builder.TestCreateChatEventDTO;
import com.persoff68.fatodo.builder.TestCreateCommentEventDTO;
import com.persoff68.fatodo.builder.TestCreateContactEventDTO;
import com.persoff68.fatodo.builder.TestCreateEventDTO;
import com.persoff68.fatodo.builder.TestCreateItemEventDTO;
import com.persoff68.fatodo.builder.TestCreateReminderEventDTO;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.dto.create.CreateChatEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateCommentEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateItemEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateReminderEventDTO;
import com.persoff68.fatodo.model.dto.delete.DeleteContactEventsDTO;
import com.persoff68.fatodo.model.dto.delete.DeleteEventsDTO;
import com.persoff68.fatodo.model.dto.delete.DeleteUserEventsDTO;
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

import java.util.List;
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
class EventConsumerIT {

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
        CreateEventDTO dto = TestCreateEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "default", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addDefaultEvent(any(), any());
    }

    @Test
    void testAddContactEvent() throws InterruptedException {
        CreateContactEventDTO dto = TestCreateContactEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "contact", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addContactEvent(any(), any(), any());
    }

    @Test
    void testAddItemEvent() throws InterruptedException {
        CreateItemEventDTO dto = TestCreateItemEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "item", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addItemEvent(any(), any(), any(), any());
    }

    @Test
    void testAddCommentEvent() throws InterruptedException {
        CreateCommentEventDTO dto = TestCreateCommentEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "comment", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addCommentEvent(any(), any(), any());
    }

    @Test
    void testAddChatEvent() throws InterruptedException {
        CreateChatEventDTO dto = TestCreateChatEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "chat", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addChatEvent(any(), any(), any(), any());
    }

    @Test
    void testAddReminderEvent() throws InterruptedException {
        CreateReminderEventDTO dto = TestCreateReminderEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event_add", "reminder", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).addReminderEvent(any(), any(), any());
    }

    @Test
    void testDeleteGroupEventsForUsers() throws InterruptedException {
        DeleteUserEventsDTO dto = new DeleteUserEventsDTO(UUID.randomUUID(), List.of(UUID.randomUUID()));
        kafkaTemplate.send("event_delete", "group-delete-users", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).deleteGroupEventsForUser(any(), any());
    }

    @Test
    void testDeleteChatEventsForUsers() throws InterruptedException {
        DeleteUserEventsDTO dto = new DeleteUserEventsDTO(UUID.randomUUID(), List.of(UUID.randomUUID()));
        kafkaTemplate.send("event_delete", "chat-delete-users", dto);
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
        verify(eventService, times(1)).deleteGroupEvents(any());
    }

    @Test
    void testDeleteItemEvents() throws InterruptedException {
        DeleteEventsDTO dto = new DeleteEventsDTO(UUID.randomUUID());
        kafkaTemplate.send("event_delete", "item-delete", dto);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventService, times(1)).deleteItemEvents(any());
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
