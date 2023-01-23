package com.my.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Getter
@Component
@RefreshScope
public class PropertiesConfig {
    @Value("${app.service.name}")
    private String name;

    @Value("${app.products.data.path}")
    private String productsDataPath;

    @Value("${app.products.data.collection}")
    private String productsCollection;

    @Value("${kafka.productSearch.bootstrapAddress}")
    private String kafkaServer;

    @Value("${kafka.productSearch.topic.product.name}")
    private String kafkaTopicProductName;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String securityJwkSetUri;
}
