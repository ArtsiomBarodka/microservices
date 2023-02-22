package com.my.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Getter
@Component
@RefreshScope
public class PropertiesConfig {

    @Value("${app.client.products.host}")
    private String productsHost;

    @Value("${app.client.customers.host}")
    private String customersHost;

    @Value("${kafka.orderOrchestrator.bootstrapAddress}")
    private String kafkaServer;

    @Value("${kafka.orderOrchestrator.topic.newOrder.name}")
    private String kafkaTopicNewOrderName;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String securityJwkSetUri;
}
