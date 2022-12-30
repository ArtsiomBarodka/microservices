package com.my.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class OrderOrchestratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderOrchestratorApplication.class, args);
    }
}
