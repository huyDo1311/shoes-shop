package com.cybersoft.shop.response.order;

import com.cybersoft.shop.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private int id;
    private String userEmail;
    private OrderStatus status;
    private float total;
    List<OrderItemResponse> items;
}
