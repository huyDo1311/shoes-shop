package com.cybersoft.shop.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    Pending,
    Confirmed,
    Shipped,
    Delivered,
    Cancelled
}
