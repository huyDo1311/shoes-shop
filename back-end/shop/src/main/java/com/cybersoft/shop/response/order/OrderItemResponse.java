package com.cybersoft.shop.response.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    private int id;
    private String sku;

    private int productId;
    private String productName;
    private String imageUrl;

    private String colorName;
    private int sizeValue;

    private int quantity;
    private float price;
    private float lineTotal;
}
