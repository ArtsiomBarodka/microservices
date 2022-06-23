package com.my.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComponentsConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
