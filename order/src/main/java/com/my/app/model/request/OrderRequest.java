package com.my.app.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @Min(1)
    @NotNull
    private Long userId;

    @NotEmpty
    private List<@NotNull OrderItemRequest> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemRequest {
        @NotBlank
        private String productId;

        @Min(1)
        @NotNull
        private Integer count;
    }
}
