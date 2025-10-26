package com.cybersoft.shop.request;

import com.cybersoft.shop.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdateRequest {
    @NotNull
    private OrderStatus status;
}
