package com.my.app.service;

import com.my.app.model.dto.OrderDto;
import org.springframework.lang.NonNull;

public interface OrderProcessNotificationService {
    void startOrderProcess(@NonNull OrderDto newOrder);
}
