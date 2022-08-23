package com.persoff68.fatodo.web.kafka;

import com.persoff68.fatodo.builder.TestEventDTO;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.service.EventDefaultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

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
    private EventDefaultService eventDefaultService;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeEach
    void setup() {
        kafkaTemplate = buildKafkaTemplate();
    }

    @Test
    void testAddEvent() throws InterruptedException {
        EventDTO eventDTO = TestEventDTO.defaultBuilder().build().toParent();
        kafkaTemplate.send("event", eventDTO);
        boolean messageConsumed = mailConsumer.getLatch().await(5, TimeUnit.SECONDS);

        assertThat(messageConsumed).isTrue();
        verify(eventDefaultService, times(1)).addEvent(any(), any(), any());
    }

    private <T> KafkaTemplate<String, T> buildKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(embeddedKafkaBroker.getBrokersAsString());
    }

}
