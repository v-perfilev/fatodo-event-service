package com.persoff68.fatodo.web.kafka;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.service.EventService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {
        "kafka.bootstrapAddress=localhost:9092",
        "kafka.groupId=test",
        "kafka.partitions=1",
        "kafka.autoOffsetResetConfig=earliest"
})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class WsProducerIT {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    EventService eventService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @SpyBean
    WsServiceClient wsServiceClient;

    private ConcurrentMessageListenerContainer<String, WsEventDTO<EventDTO>> wsContainer;
    private BlockingQueue<ConsumerRecord<String, WsEventDTO<EventDTO>>> wsRecords;

    @BeforeEach
    void setup() {
        startWsConsumer();
    }

    @AfterEach
    void cleanup() {
        eventRepository.deleteAll();

        stopWsConsumer();
    }

    @Test
    void testSendEvent() throws Exception {
        List<UUID> recipientIdList = List.of(UUID.randomUUID());
        eventService.addDefaultEvent(EventType.WELCOME, recipientIdList);

        ConsumerRecord<String, WsEventDTO<EventDTO>> record = wsRecords.poll(10, TimeUnit.SECONDS);

        assertThat(wsServiceClient instanceof WsProducer).isTrue();
        assertThat(record).isNotNull();
        assertThat(record.topic()).isEqualTo("ws_event");
        verify(wsServiceClient).sendEvent(any());
    }

    private void startWsConsumer() {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(WsEventDTO.class, EventDTO.class);
        ConcurrentKafkaListenerContainerFactory<String, WsEventDTO<EventDTO>> jsonContainerFactory =
                KafkaUtils.buildJsonContainerFactory(embeddedKafkaBroker.getBrokersAsString(),
                        "test", "earliest", javaType);
        wsContainer = jsonContainerFactory.createContainer("ws_event");
        wsRecords = new LinkedBlockingQueue<>();
        wsContainer.setupMessageListener((MessageListener<String, WsEventDTO<EventDTO>>) wsRecords::add);
        wsContainer.start();
        ContainerTestUtils.waitForAssignment(wsContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopWsConsumer() {
        wsContainer.stop();
    }


}
