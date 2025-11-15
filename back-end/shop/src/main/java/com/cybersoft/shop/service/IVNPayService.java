package com.cybersoft.shop.service;

import com.cybersoft.shop.request.PaymentRequest;
import com.cybersoft.shop.request.QueryRequest;
import com.cybersoft.shop.request.RefundRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface IVNPayService {
    String createPaymentUrl(PaymentRequest paymentRequest, HttpServletRequest request);
    Map<String, String> handleVnpayIPN(HttpServletRequest request);
    Map<String, Object> queryDr(QueryRequest req, HttpServletRequest servletReq);
    Map<String, Object> refund(RefundRequest req, HttpServletRequest servletReq);

}
