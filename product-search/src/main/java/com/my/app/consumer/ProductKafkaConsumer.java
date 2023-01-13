package com.my.app.consumer;

import com.epam.app.model.message.ProductsMessage;
import com.my.app.model.converter.FromMessageToDtoListProductsConverter;
import com.my.app.model.dto.ProductDto;
import com.my.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductKafkaConsumer {
    private final ProductService productService;
    private final FromMessageToDtoListProductsConverter toDtoListProductsConverter;

    @KafkaListener(topics = "#{'${kafka.productSearch.topic.product.name}'.split(',')}", containerFactory = "productsKafkaListenerContainerFactory")
    public void processProductsUpdate(ProductsMessage productsMessage,
                                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                      @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String msgKey) {
        log.info("New Product is received: topic = {}, key = {}, product: {}", topic, msgKey, productsMessage);

        final List<ProductDto> productDtoList = toDtoListProductsConverter.convert(productsMessage);
        productService.updateAllProducts(productDtoList);
    }
}
