package com.my.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Getter
@Component
@RefreshScope
public class PropertiesConfig {
    @Value("${kafka.orderOrchestrator.bootstrapAddress}")
    private String kafkaServer;

    @Value("${kafka.orderOrchestrator.groupId}")
    private String kafkaConsumerGroupId;

    @Value("${kafka.orderOrchestrator.topic.newOrder.name}")
    private String kafkaTopicNewOrderName;

    @Value("${kafka.orderOrchestrator.topic.newOrder.partitions}")
    private int kafkaTopicNewOrderPartitions;

    @Value("${kafka.orderOrchestrator.consumer.newOrder.count}")
    private int kafkaConsumerNewOrderCount;

    @Value("${app.operation.retry.count}")
    private int operationRetriesCount;

    @Value("${app.client.products.host}")
    private String productsHost;

    @Value("${app.client.customers.host}")
    private String customersHost;

    @Value("${app.client.orders.host}")
    private String ordersHost;
}
