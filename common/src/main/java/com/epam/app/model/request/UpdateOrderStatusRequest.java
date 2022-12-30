package com.epam.app.model.request;

import com.epam.app.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {
    @Min(1)
    @NotNull
    private Long id;

    @NotNull
    private OrderStatus orderStatus;
}
