package com.cybersoft.shop.controller;
import com.cybersoft.shop.request.PaymentRequest;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.service.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private IVNPayService vnPayService;

    @PostMapping("/create_payment_url")
    public ResponseEntity<ResponseObject> createPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        try {
            String paymentUrl = vnPayService.createPaymentUrl(paymentRequest, request);

            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK.value())
                    .message("Payment URL generated successfully.")
                    .data(paymentUrl)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error generating payment URL: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/vnpay-ipn")
    public ResponseEntity<String> handleVnpayIPN(HttpServletRequest request) {
//        String result = vnPayService.handleVnpayIPN(request);
//        return ResponseEntity.ok(result);

        // ✅ Lấy toàn bộ params từ request
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            fields.put(fieldName, fieldValue);
        }

        // ✅ Log ra console
        System.out.println("====== VNPAY IPN CALLBACK ======");
        fields.forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("================================");

        // ✅ Gọi service xử lý
        String result = vnPayService.handleVnpayIPN(request);
        return ResponseEntity.ok(result);
    }

}
