package com.persoff68.fatodo.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.config.constant.KafkaTopics;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@EnableKafka
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
@RequiredArgsConstructor
public class KafkaConfiguration {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.groupId}")
    private String groupId;

    @Value(value = "${kafka.partitions}")
    private int partitions;

    @Value(value = "${kafka.autoOffsetResetConfig:latest}")
    private String autoOffsetResetConfig;


    private final ObjectMapper objectMapper;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return KafkaUtils.buildKafkaAdmin(bootstrapAddress);
    }

    @Bean
    public NewTopic eventNewTopic() {
        return KafkaUtils.buildTopic(KafkaTopics.EVENT.getValue(), partitions);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventDTO> eventContainerFactory() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(EventDTO.class);
        return KafkaUtils.buildJsonContainerFactory(bootstrapAddress, groupId, autoOffsetResetConfig, javaType);
    }

}
