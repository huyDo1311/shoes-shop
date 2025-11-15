package com.cybersoft.shop.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REFUND
}
