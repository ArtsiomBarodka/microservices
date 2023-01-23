package com.my.app.service.impl;

import com.epam.app.constants.KafkaCustomHeaders;
import com.epam.app.model.message.OrderMessage;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.converter.FromDtoToMessageOrderConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.service.OrderProcessNotificationService;
import com.my.app.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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
        final Message<OrderMessage> hello = MessageBuilder.withPayload(toMessageOrderConverter.convert(newOrder))
                .setHeader(KafkaHeaders.TOPIC, propertiesConfig.getKafkaTopicNewOrderName())
                .setHeader(KafkaHeaders.MESSAGE_KEY, String.valueOf(newOrder.getId()))
                .setHeader(KafkaCustomHeaders.AUTHORIZATION, SecurityUtils.getAuthToken())
                .build();

        kafkaTemplate.send(hello);
    }
}
