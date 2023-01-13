package com.my.app.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {
    private final PropertiesConfig appPropertiesConfig;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, appPropertiesConfig.getKafkaServer());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic productTopic() {
        return TopicBuilder.name(appPropertiesConfig.getKafkaTopicProductName())
                .partitions(appPropertiesConfig.getKafkaTopicProductPartitions())
                .compact()
                .build();
    }
}
