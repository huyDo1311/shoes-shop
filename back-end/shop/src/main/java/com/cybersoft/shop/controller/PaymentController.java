package com.cybersoft.shop.controller;
import com.cybersoft.shop.component.VNPayUtils;
import com.cybersoft.shop.request.PaymentRequest;
import com.cybersoft.shop.request.QueryRequest;
import com.cybersoft.shop.request.RefundRequest;
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

    @Autowired
    private VNPayUtils vnPayUtils;

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

    //    @PostMapping("/vnpay-ipn")
//    public ResponseEntity<String> handleVnpayIPN(HttpServletRequest request) {
//        String result = vnPayService.handleVnpayIPN(request);
//        return ResponseEntity.ok(result);
//    }
    @GetMapping("/vnpay-ipn")
    public ResponseEntity<Map<String, String>> handleVnpayIPNGet(HttpServletRequest request) {
        Map<String, String> res = vnPayService.handleVnpayIPN(request);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/vnpay-ipn")
    public ResponseEntity<Map<String, String>> handleVnpayIPNPost(HttpServletRequest request) {
        Map<String, String> res = vnPayService.handleVnpayIPN(request);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<Void> vnpayReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
            String k = e.nextElement();
            fields.put(k, request.getParameter(k));
        }
        System.out.println("===== VNPay Return Params =====");
        fields.forEach((k,v) -> System.out.println(k + " = " + v));
        System.out.println("================================");
        boolean ok = vnPayUtils.validateSignature(fields);
        String code = fields.get("vnp_ResponseCode");
//        String redirectTo = "http://localhost:3000/payment/payment-callback?status="
//                + (ok && "00".equals(code) ? "success" : "failed")
//                + "&vnp_TxnRef=" + fields.getOrDefault("vnp_TxnRef","");

        String redirectTo = "http://localhost:8080/payments/return-view"
                + "?status=" + ("00".equals(code) && ok ? "success" : "failed")
                + "&vnp_TxnRef=" + fields.getOrDefault("vnp_TxnRef","")
                + "&code=" + (code == null ? "" : code)
                + "&sigOk=" + ok;

        // 302 redirect về FE
        return ResponseEntity.status(302).header("Location", redirectTo).build();
    }

    @GetMapping("/return-view")
    public ResponseEntity<String> returnView(
            @RequestParam(defaultValue = "failed") String status,
            @RequestParam(name = "vnp_TxnRef", defaultValue = "") String txnRef,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "sigOk", defaultValue = "false") String sigOk) {

        String html = """
      <html><body style='font-family: sans-serif'>
        <h2>VNPay: %s</h2>
        <p>TxnRef: %s</p>
        <p>vnp_ResponseCode: %s</p>
        <p>Signature valid: %s</p>
      </body></html>
    """.formatted("success".equals(status) ? "Thanh toán thành công" : "Thanh toán thất bại",
                txnRef, code, sigOk);

        return ResponseEntity.ok(html);
    }

    @PostMapping("/query")
    public ResponseEntity<?> query(@RequestBody QueryRequest req, HttpServletRequest servletReq) {
        Map<String, Object> result = vnPayService.queryDr(req, servletReq);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refund")
    public ResponseEntity<?> refund(@RequestBody RefundRequest req, HttpServletRequest servletReq) {
        Map<String, Object> result = vnPayService.refund(req, servletReq);
        return ResponseEntity.ok(result);
    }



}
