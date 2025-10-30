package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.component.VNPayConfig;
import com.cybersoft.shop.component.VNPayUtils;
import com.cybersoft.shop.entity.Order;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.enums.OrderStatus;
import com.cybersoft.shop.repository.OrderRepository;
import com.cybersoft.shop.repository.UserRepository;
import com.cybersoft.shop.request.PaymentRequest;
import com.cybersoft.shop.service.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IVNPayServiceImp implements IVNPayService {

    @Autowired
    private VNPayConfig vnPayConfig;
    @Autowired
    private VNPayUtils vnPayUtils;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public String createPaymentUrl(PaymentRequest paymentRequest, HttpServletRequest request) {
        String version = "2.1.0";
        String command = "pay";
        String orderType = "other";
        long amount = paymentRequest.getAmount() * 100; // Số tiền cần nhân với 100
        String bankCode = paymentRequest.getBankCode();

        String email = paymentRequest.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found: " + email));

        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.Pending)
                .orElseThrow(()-> new RuntimeException("No pending cart for user"));

        String transactionReference = vnPayUtils.getRandomNumber(8); // Mã giao dịch
        order.setVnpTxnRef(transactionReference);
        // 🔹 Lưu lại mã vnp_TxnRef trong DB
        orderRepository.save(order);

        String clientIpAddress = vnPayUtils.getIpAddress(request);
        String terminalCode = vnPayConfig.getVnpTmnCode();

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", version);
        params.put("vnp_Command", command);
        params.put("vnp_TmnCode", terminalCode);
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            params.put("vnp_BankCode", bankCode);
        }
        params.put("vnp_TxnRef", transactionReference);
        params.put("vnp_OrderInfo", "Thanh toan don hang:" + transactionReference);
        params.put("vnp_OrderType", orderType);

        String locale = paymentRequest.getLanguage();
        if (locale != null && !locale.isEmpty()) {
            params.put("vnp_Locale", locale);
        } else {
            params.put("vnp_Locale", "vn");
        }

        params.put("vnp_ReturnUrl", vnPayConfig.getVnpReturnUrl());
        params.put("vnp_IpAddr", clientIpAddress);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String createdDate = dateFormat.format(calendar.getTime());
        params.put("vnp_CreateDate", createdDate);

        calendar.add(Calendar.MINUTE, 15);
        String expirationDate = dateFormat.format(calendar.getTime());
        params.put("vnp_ExpireDate", expirationDate);

        List<String> sortedFieldNames = new ArrayList<>(params.keySet());
        Collections.sort(sortedFieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder queryData = new StringBuilder();

        for (Iterator<String> iterator = sortedFieldNames.iterator(); iterator.hasNext();) {
            String fieldName = iterator.next();
            String fieldValue = params.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                queryData.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (iterator.hasNext()) {
                    hashData.append('&');
                    queryData.append('&');
                }
            }
        }

        String secureHash = vnPayUtils.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());
        queryData.append("&vnp_SecureHash=").append(secureHash);

        return vnPayConfig.getVnpPayUrl() + "?" + queryData;
    }
}
