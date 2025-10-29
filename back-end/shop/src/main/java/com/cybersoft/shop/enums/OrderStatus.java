package com.cybersoft.shop.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    Pending,
    WaitConfirm,
    Confirmed,
    Shipped,
    Delivered,
    DeliveryFailed,
    Cancelled;

}
