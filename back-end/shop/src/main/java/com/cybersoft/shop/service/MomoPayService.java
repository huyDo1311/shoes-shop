package com.cybersoft.shop.service;

import com.cybersoft.shop.request.PaymentRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface MomoPayService {
    String createMomoPaymentUrl(PaymentRequest paymentRequest, HttpServletRequest request);
}
