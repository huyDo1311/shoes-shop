package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.component.MOMOConfig;
import com.cybersoft.shop.component.MomoPayUtils;
import com.cybersoft.shop.entity.Order;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.enums.OrderStatus;
import com.cybersoft.shop.repository.OrderRepository;
import com.cybersoft.shop.repository.UserRepository;
import com.cybersoft.shop.request.PaymentRequest;
import com.cybersoft.shop.service.MomoPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MomoPayServiceImp implements MomoPayService {

    @Autowired
    private MOMOConfig momoConfig;
    @Autowired
    private MomoPayUtils momoPayUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public String createMomoPaymentUrl(PaymentRequest paymentRequest, HttpServletRequest request) {
        try {
            // 🔹 Lấy user & order
            String email = paymentRequest.getEmail();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found: " + email));

            Order order = orderRepository.findByUserAndStatus(user, OrderStatus.Pending)
                    .orElseThrow(() -> new RuntimeException("No pending cart for user"));

            String orderId = UUID.randomUUID().toString();
            order.setMomoTxnRef(orderId);
            orderRepository.save(order);

            long amount = paymentRequest.getAmount(); // MoMo dùng đơn vị VNĐ, không nhân 100

            String requestId = UUID.randomUUID().toString();
            String requestType = "captureWallet";

            String extraData = Base64.getEncoder().encodeToString(
                    ("email=" + paymentRequest.getEmail()).getBytes()
            );

            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("partnerCode", momoConfig.getPartnerCode());
            requestBody.put("requestType", requestType);
            requestBody.put("redirectUrl", momoConfig.getMomoReturnUrl());
            requestBody.put("orderId", orderId);
            requestBody.put("amount", amount);
            requestBody.put("orderInfo", "Thanh toán đơn hàng " + orderId);
            requestBody.put("requestId", requestId);
            requestBody.put("extraData", extraData);
            requestBody.put("partnerName", "Shoes Shop");
            requestBody.put("storeId", "MomoStore");
            requestBody.put("ipnUrl", momoConfig.getIpnUrl());
            requestBody.put("lang", "vi");

            String rawSignature = "accessKey=" + momoConfig.getAccessKey()
                    + "&amount=" + amount
                    + "&extraData=" + extraData
                    + "&ipnUrl=" + momoConfig.getIpnUrl()
                    + "&orderId=" + orderId
                    + "&orderInfo=" + "Thanh toán đơn hàng " + orderId
                    + "&partnerCode=" + momoConfig.getPartnerCode()
                    + "&redirectUrl=" + momoConfig.getMomoReturnUrl()
                    + "&requestId=" + requestId
                    + "&requestType=" + requestType;

            String signature = momoPayUtils.hmacSHA256(rawSignature, momoConfig.getSecretKey());
            requestBody.put("signature", signature);

            // 🔹 Gửi request tới MoMo
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    momoConfig.getMomoUrl() + "/create",
                    entity,
                    Map.class
            );

            // 🔹 Kiểm tra phản hồi từ MoMo
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                if (body.containsKey("payUrl")) {
                    return body.get("payUrl").toString();
                } else if (body.containsKey("deeplink")) { // dự phòng cho MoMo trả về deeplink
                    return body.get("deeplink").toString();
                } else {
                    throw new RuntimeException("Phản hồi MoMo không có URL thanh toán: " + body);
                }
            }

            throw new RuntimeException("Không tạo được URL thanh toán MoMo");

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo thanh toán MoMo: " + e.getMessage(), e);
        }
    }

}
