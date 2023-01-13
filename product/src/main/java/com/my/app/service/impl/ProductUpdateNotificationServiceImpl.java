package com.my.app.service.impl;

import com.epam.app.model.message.ProductsMessage;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.converter.FromDtoListToMessageProductsConverter;
import com.my.app.model.dto.ProductDto;
import com.my.app.service.ProductNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductUpdateNotificationServiceImpl implements ProductNotificationService {
    private final PropertiesConfig propertiesConfig;
    private final FromDtoListToMessageProductsConverter toMessageProductsConverter;
    private final KafkaTemplate<String, ProductsMessage> kafkaTemplate;

    @Override
    public void notify(@NonNull Collection<ProductDto> productDtoList) {
        log.info("Sending notification to Product Search service for updating products. Products: {}", productDtoList);

        kafkaTemplate.send(propertiesConfig.getKafkaTopicProductName(), UUID.randomUUID().toString(), toMessageProductsConverter.convert(productDtoList));
    }
}
