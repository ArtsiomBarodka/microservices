package com.my.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Getter
@Component
@RefreshScope
public class PropertiesConfig {
    @Value("${app.elasticSearch.uri}")
    private String elasticSearchUri;

    @Value("${app.products.data.path}")
    private String productsDataPath;

    @Value("${kafka.productSearch.bootstrapAddress}")
    private String kafkaServer;

    @Value("${kafka.productSearch.groupId}")
    private String kafkaConsumerGroupId;

    @Value("${kafka.productSearch.topic.product.name}")
    private String kafkaTopicProductName;

    @Value("${kafka.productSearch.topic.product.partitions}")
    private int kafkaTopicProductPartitions;

    @Value("${kafka.productSearch.consumer.product.count}")
    private int kafkaConsumerProductCount;
}
