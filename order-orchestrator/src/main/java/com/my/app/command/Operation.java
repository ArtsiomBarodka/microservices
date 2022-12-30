package com.my.app.command;

import com.my.app.model.dto.OrderDto;
import org.springframework.lang.NonNull;

public interface Operation {
    @NonNull OperationResultStatus process(@NonNull OrderDto orderDto);
    @NonNull OperationResultStatus revert(@NonNull OrderDto orderDto);
}
