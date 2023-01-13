package com.my.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@RequiredArgsConstructor
@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    private final PropertiesConfig propertiesConfig;

    @Bean
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(propertiesConfig.getElasticSearchUri())
                .build();
    }
}
