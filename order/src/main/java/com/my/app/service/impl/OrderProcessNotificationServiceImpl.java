package com.my.app.service.impl;

import com.epam.app.model.message.OrderMessage;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.converter.FromDtoToMessageOrderConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.service.OrderProcessNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessNotificationServiceImpl implements OrderProcessNotificationService {
    private final PropertiesConfig propertiesConfig;
    private final FromDtoToMessageOrderConverter toMessageOrderConverter;
    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;

    @Override
    public void startOrderProcess(@NonNull OrderDto newOrder) {
        log.info("Sending notification to Order-Orchestrator for Order: {}", newOrder);
        kafkaTemplate.send(propertiesConfig.getKafkaTopicNewOrderName(), String.valueOf(newOrder.getId()), toMessageOrderConverter.convert(newOrder));
    }
}
