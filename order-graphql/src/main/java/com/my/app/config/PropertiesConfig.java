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

    @Value("${app.client.orders.host}")
    private String ordersHost;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String securityJwkSetUri;
}
