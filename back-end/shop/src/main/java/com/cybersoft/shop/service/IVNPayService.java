package com.cybersoft.shop.service;

import com.cybersoft.shop.request.PaymentRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface IVNPayService {
    String createPaymentUrl(PaymentRequest paymentRequest, HttpServletRequest request);
}
