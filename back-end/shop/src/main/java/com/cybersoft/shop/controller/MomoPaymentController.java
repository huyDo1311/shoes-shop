package com.cybersoft.shop.controller;

import com.cybersoft.shop.request.PaymentRequest;
import com.cybersoft.shop.request.VNPStatusUpdateRequest;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.response.order.OrderResponse;
import com.cybersoft.shop.service.IVNPayService;
import com.cybersoft.shop.service.MomoPayService;
import com.cybersoft.shop.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class MomoPaymentController {
    @Autowired
    private MomoPayService momoPayService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/momo")
    public ResponseEntity<ResponseObject> createMomoPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        try {
            String paymentUrl = momoPayService.createMomoPaymentUrl(paymentRequest, request);

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

    @PostMapping("/momo/status")
    public ResponseEntity<?> updateStatusVNP(@RequestBody VNPStatusUpdateRequest request){

        OrderResponse data = orderService.VNPStatusUpdate(request);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("update success")
                        .status(HttpStatus.OK.value())
                        .data(data)
                        .build()
        );
    }
}
