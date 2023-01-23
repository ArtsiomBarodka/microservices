package com.my.app.consumer;

import com.epam.app.constants.KafkaCustomHeaders;
import com.epam.app.model.message.OrderMessage;
import com.my.app.model.converter.FromMessageToOrderDtoConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaConsumer {
    private final FromMessageToOrderDtoConverter toOrderDtoConverter;
    private final OrderService orderService;

    @KafkaListener(topics = "#{'${kafka.orderOrchestrator.topic.newOrder.name}'.split(',')}", containerFactory = "orderKafkaListenerContainerFactory")
    public void processOrder(OrderMessage orderMessage,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String msgKey,
                             @Header(KafkaCustomHeaders.AUTHORIZATION) String token) {
        final Long orderId = orderMessage.getOrderId();
        log.info("Authorization Header is received: Token = {}", token);
        log.info("new created Order is receive: topic = {}, key = {}, orderId = {}", topic, msgKey, orderId);

        final OrderDto orderDto = toOrderDtoConverter.convert(orderMessage);
        orderService.processOrder(orderDto, token);
    }
}
