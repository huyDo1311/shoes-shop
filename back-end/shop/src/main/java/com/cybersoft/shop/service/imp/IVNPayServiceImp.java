package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.component.VNPayConfig;
import com.cybersoft.shop.component.VNPayUtils;
import com.cybersoft.shop.entity.Order;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.enums.OrderStatus;
import com.cybersoft.shop.enums.PaymentMethod;
import com.cybersoft.shop.enums.PaymentStatus;
import com.cybersoft.shop.repository.OrderRepository;
import com.cybersoft.shop.repository.UserRepository;
import com.cybersoft.shop.request.PaymentRequest;
import com.cybersoft.shop.request.QueryRequest;
import com.cybersoft.shop.request.RefundRequest;
import com.cybersoft.shop.request.VNPStatusUpdateRequest;
import com.cybersoft.shop.service.IVNPayService;
import com.cybersoft.shop.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private OrderService orderService;

//    @Override
//    public String createPaymentUrl(PaymentRequest paymentRequest, HttpServletRequest request) {
//        String version = "2.1.0";
//        String command = "pay";
//        String orderType = "other";
//        long amount = paymentRequest.getAmount() * 100; // Số tiền cần nhân với 100
//        String bankCode = paymentRequest.getBankCode();
//
//        String email = paymentRequest.getEmail();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(()-> new RuntimeException("User not found: " + email));
//
//        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.Pending)
//                .orElseThrow(()-> new RuntimeException("No pending cart for user"));
//
//        String transactionReference = vnPayUtils.getRandomNumber(8); // Mã giao dịch
//        order.setVnpTxnRef(transactionReference);
//        // 🔹 Lưu lại mã vnp_TxnRef trong DB
//        orderRepository.save(order);
//
//        String clientIpAddress = vnPayUtils.getIpAddress(request);
//        String terminalCode = vnPayConfig.getVnpTmnCode();
//
//        Map<String, String> params = new HashMap<>();
//        params.put("vnp_Version", version);
//        params.put("vnp_Command", command);
//        params.put("vnp_TmnCode", terminalCode);
//        params.put("vnp_Amount", String.valueOf(amount));
//        params.put("vnp_CurrCode", "VND");
//
//        if (bankCode != null && !bankCode.isEmpty()) {
//            params.put("vnp_BankCode", bankCode);
//        }
//        params.put("vnp_TxnRef", transactionReference);
//        params.put("vnp_OrderInfo", "Thanh toan don hang:" + transactionReference);
//        params.put("vnp_OrderType", orderType);
//
//        String locale = paymentRequest.getLanguage();
//        if (locale != null && !locale.isEmpty()) {
//            params.put("vnp_Locale", locale);
//        } else {
//            params.put("vnp_Locale", "vn");
//        }
//
//        params.put("vnp_ReturnUrl", vnPayConfig.getVnpReturnUrl());
//        params.put("vnp_IpAddr", clientIpAddress);
//
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        String createdDate = dateFormat.format(calendar.getTime());
//        params.put("vnp_CreateDate", createdDate);
//
//        calendar.add(Calendar.MINUTE, 15);
//        String expirationDate = dateFormat.format(calendar.getTime());
//        params.put("vnp_ExpireDate", expirationDate);
//
//        List<String> sortedFieldNames = new ArrayList<>(params.keySet());
//        Collections.sort(sortedFieldNames);
//
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder queryData = new StringBuilder();
//
//        for (Iterator<String> iterator = sortedFieldNames.iterator(); iterator.hasNext();) {
//            String fieldName = iterator.next();
//            String fieldValue = params.get(fieldName);
//
//            if (fieldValue != null && !fieldValue.isEmpty()) {
//                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
//                queryData.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
//                        .append('=')
//                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
//                if (iterator.hasNext()) {
//                    hashData.append('&');
//                    queryData.append('&');
//                }
//            }
//        }
//
//        String secureHash = vnPayUtils.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());
//        queryData.append("&vnp_SecureHash=").append(secureHash);
//
//        return vnPayConfig.getVnpPayUrl() + "?" + queryData;
//    }

    @Override
    public String createPaymentUrl(PaymentRequest paymentRequest, HttpServletRequest request) {
        String version = "2.1.0";
        String command = "pay";
        String orderType = "other";

        String email = paymentRequest.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.Pending)
                .orElseThrow(() -> new RuntimeException("No pending cart for user"));

        order.recalcTotal(); // cập nhật total từ items
        BigDecimal totalVnd = new BigDecimal(Float.toString(order.getTotal()))
                .setScale(0, RoundingMode.HALF_UP);
        long expectedAmountXu = totalVnd.multiply(BigDecimal.valueOf(100L)).longValueExact();

        String transactionReference = vnPayUtils.getRandomNumber(8);
        order.setVnpTxnRef(transactionReference);
        order.setVnpExpectedAmount(expectedAmountXu);
        orderRepository.save(order);

        String bankCode = paymentRequest.getBankCode();
        String clientIpAddress = vnPayUtils.getIpAddress(request);
        String terminalCode = vnPayConfig.getVnpTmnCode();

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", version);
        params.put("vnp_Command", command);
        params.put("vnp_TmnCode", terminalCode);
        params.put("vnp_Amount", String.valueOf(expectedAmountXu));
        params.put("vnp_CurrCode", "VND");
        if (bankCode != null && !bankCode.isEmpty()) {
            params.put("vnp_BankCode", bankCode);
        }
        params.put("vnp_TxnRef", transactionReference);
        params.put("vnp_OrderInfo", "Thanh toan don hang:" + transactionReference);
        params.put("vnp_OrderType", orderType);

        String locale = (paymentRequest.getLanguage() != null && !paymentRequest.getLanguage().isEmpty())
                ? paymentRequest.getLanguage() : "vn";
        params.put("vnp_Locale", locale);

        params.put("vnp_ReturnUrl", vnPayConfig.getVnpReturnUrl());
        params.put("vnp_IpAddr", clientIpAddress);

        // 5) Thời gian tạo/hết hạn (Asia/Ho_Chi_Minh)
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String createdDate = dateFormat.format(calendar.getTime());
        params.put("vnp_CreateDate", createdDate);
        order.setVnpCreateDate(createdDate);   // <--- NEW
        orderRepository.save(order);


        calendar.add(Calendar.MINUTE, 15);
        String expirationDate = dateFormat.format(calendar.getTime());
        params.put("vnp_ExpireDate", expirationDate);

        // 6) Ký & build query
        List<String> sortedFieldNames = new ArrayList<>(params.keySet());
        Collections.sort(sortedFieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder queryData = new StringBuilder();

        for (Iterator<String> iterator = sortedFieldNames.iterator(); iterator.hasNext();) {
            String fieldName = iterator.next();
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
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


    @Override
    public Map<String, String> handleVnpayIPN(HttpServletRequest request) {
        Map<String, String> resp = new HashMap<>();
        try{
            Map<String, String> fields = new HashMap<>();
            for(Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();){
                String name = params.nextElement();
                String value = request.getParameter(name);
                fields.put(name,value);
            }

            // valid chữ ký
            boolean valid = vnPayUtils.validateSignature(fields);
            if(!valid){
                resp.put("RspCode", "97");
                resp.put("Message", "Invalid signature");
                return resp;
            }

            // lấy thông tin
            String responseCode = fields.get("vnp_ResponseCode");
            String txnRef = fields.get("vnp_TxnRef");
            String amountStr = fields.get("vnp_Amount");
            String bankCode = fields.get("vnp_BankCode");
            String transNo = fields.get("vnp_TransactionNo");
            String payDate = fields.get("vnp_PayDate");

            // tìm order theo txnRef
            Optional<Order> opt = orderRepository.findByVnpTxnRef(txnRef);
            if(opt.isEmpty()){
                resp.put("RspCode", "01");
                resp.put("Message", "Order not found");
                return resp;
            }
            Order order = opt.get();


            if(order.getStatus() != OrderStatus.Pending && order.getStatus() != OrderStatus.WaitConfirm){
                resp.put("RspCode", "00");
                resp.put("Message", "Order already confỉrmed");
                return resp;
            }

            long amountFromVnPay = Long.parseLong(amountStr);
            if (!Objects.equals(order.getVnpExpectedAmount(), amountFromVnPay)) {
                resp.put("RspCode", "02");
                resp.put("Message", "Invalid amount");
                return resp;
            }

            if ("00".equals(responseCode)) {
                order.setPaymentMethod(PaymentMethod.VNPay);
                order.setPaymentStatus(PaymentStatus.SUCCESS);
                order.setVnpTransactionNo(transNo);
                order.setVnpBankCode(bankCode);
                order.setVnpPayDate(payDate);
                orderRepository.save(order);
                VNPStatusUpdateRequest upd = new VNPStatusUpdateRequest();
                upd.setVnp_TxnRef(txnRef);
                orderService.VNPStatusUpdate(upd);


            } else {
                order.setPaymentMethod(PaymentMethod.VNPay);
                order.setPaymentStatus(PaymentStatus.FAILED);
                orderRepository.save(order);
            }
            resp.put("RspCode", "00");
            resp.put("Message", "Confirm Success");
            return resp;

        }catch (Exception e){
            resp.put("RspCode", "99");
            resp.put("Message", "Unknown error: " + e.getMessage());
            return resp;
        }
    }

    @Override
    public Map<String, Object> queryDr(QueryRequest req, HttpServletRequest servletReq) {
        Map<String, String> data = new LinkedHashMap<>();
        String requestId = System.currentTimeMillis() + String.valueOf((int)(Math.random()*100000));
        data.put("vnp_RequestId", requestId);
        data.put("vnp_Version", "2.1.0");
        data.put("vnp_Command", "querydr");
        data.put("vnp_TmnCode", vnPayConfig.getVnpTmnCode());
        data.put("vnp_TxnRef", req.getTxnRef());
        if (req.getTransactionDate() != null) data.put("vnp_TransactionDate", req.getTransactionDate());
        if (req.getOrderInfo() != null)      data.put("vnp_OrderInfo", req.getOrderInfo());
        data.put("vnp_CreateDate", nowGmt7());

        String ip = vnPayUtils.getIpAddress(servletReq);
        if (ip != null && ip.contains(":")) ip = "127.0.0.1";
        data.put("vnp_IpAddr", ip);

        data.put("vnp_SecureHashType", "HmacSHA512");

        String raw = buildQueryHashData(data);
        String secureHash = vnPayUtils.hmacSHA512(vnPayConfig.getSecretKey().trim(), raw);
        data.put("vnp_SecureHash", secureHash);

        System.out.println("[QUERYDR] raw=" + raw);
        System.out.println("[QUERYDR] hash=" + secureHash);

        return httpPostJson(vnPayConfig.getVnpApiUrl(), data);
    }



    @Override
    public Map<String, Object> refund(RefundRequest req, HttpServletRequest servletReq) {

        Order order = orderRepository.findByVnpTxnRef(req.getTxnRef())
                .orElseThrow(() -> new RuntimeException("Order not found for refund"));
        Map<String, String> data = new LinkedHashMap<>();
        String requestId = System.currentTimeMillis() + String.valueOf((int) (Math.random() * 100000));
        data.put("vnp_RequestId", requestId);
        data.put("vnp_Version", "2.1.0");
        data.put("vnp_Command", "refund");
        data.put("vnp_TmnCode", vnPayConfig.getVnpTmnCode());
        data.put("vnp_TransactionType", req.getTransactionType());
        data.put("vnp_TxnRef", req.getTxnRef());

        if (order.getVnpTransactionNo() != null) {
            data.put("vnp_TransactionNo", order.getVnpTransactionNo());
        }
        String transType = req.getTransactionType();
        if ("02".equals(transType)) {
            // Hoàn toàn bộ
            Long expected = order.getVnpExpectedAmount();
            if (expected == null) {
                throw new RuntimeException("Missing expected amount for full refund");
            }
            data.put("vnp_Amount", String.valueOf(expected));
        } else if ("03".equals(transType)) {
            // Hoàn một phần
            data.put("vnp_Amount", String.valueOf(req.getAmount() * 100));
        } else {
            throw new RuntimeException("Unsupported transactionType: " + transType);
        }

        data.put("vnp_OrderInfo", req.getOrderInfo());
        data.put("vnp_TransactionDate", req.getTransactionDate());
        String createBy = (order.getUser() != null && order.getUser().getEmail() != null)
                ? order.getUser().getEmail()
                : "system";
        data.put("vnp_CreateBy", createBy);
        data.put("vnp_CreateDate", nowGmt7());
        String ip = vnPayUtils.getIpAddress(servletReq);
        if (ip != null && ip.contains(":")) ip = "127.0.0.1";
        data.put("vnp_IpAddr", ip);
        data.put("vnp_SecureHashType", "HmacSHA512");
        String raw = buildRefundHashData(data); // PIPE: requestId|version|...|orderInfo
        String secureHash = vnPayUtils.hmacSHA512(vnPayConfig.getSecretKey().trim(), raw);
        data.put("vnp_SecureHash", secureHash);
        System.out.println("[REFUND] raw=" + raw);
        System.out.println("[REFUND] hash=" + secureHash);
        Map<String, Object> resp = httpPostJson(vnPayConfig.getVnpApiUrl(), data);
        Object code = resp.get("vnp_ResponseCode");
        if (code != null && "00".equals(code.toString())) {
            orderService.updateStatus(order.getId(), OrderStatus.Cancelled.name());
            order.setPaymentStatus(PaymentStatus.REFUND);
            orderRepository.save(order);
        }
        return resp;
    }


    private String nowGmt7() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        return df.format(new Date());
    }

    private String buildDataToHash(Map<String, String> data) {
        List<String> keys = new ArrayList<>(data.keySet());
        Collections.sort(keys);
        List<String> pairs = new ArrayList<>();
        for (String k : keys) {
            if ("vnp_SecureHash".equals(k) || "vnp_SecureHashType".equals(k)) continue;
            String v = data.get(k);
            if (v != null && !v.isEmpty()) pairs.add(k + "=" + v);
        }
        return String.join("&", pairs);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> httpPostJson(String apiUrl, Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            String json = new ObjectMapper().writeValueAsString(payload);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
            String body;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                body = br.lines().collect(Collectors.joining());
            }

            if (body != null && !body.isEmpty()) {
                return new ObjectMapper().readValue(body, Map.class);
            } else {
                result.put("httpCode", code);
                result.put("message", "Empty response");
            }
        } catch (Exception e) {
            result.put("error", e.getMessage());
        } finally {
            if (conn != null) conn.disconnect();
        }
        return result;
    }

    private String buildQueryHashData(Map<String, String> d) {
        return String.join("|",
                d.getOrDefault("vnp_RequestId",""),
                d.getOrDefault("vnp_Version",""),
                d.getOrDefault("vnp_Command",""),
                d.getOrDefault("vnp_TmnCode",""),
                d.getOrDefault("vnp_TxnRef",""),
                d.getOrDefault("vnp_TransactionDate",""),
                d.getOrDefault("vnp_CreateDate",""),
                d.getOrDefault("vnp_IpAddr",""),
                d.getOrDefault("vnp_OrderInfo","")
        );
    }

    private String buildRefundHashData(Map<String, String> d) {
        return String.join("|",
                d.getOrDefault("vnp_RequestId",""),
                d.getOrDefault("vnp_Version",""),
                d.getOrDefault("vnp_Command",""),
                d.getOrDefault("vnp_TmnCode",""),
                d.getOrDefault("vnp_TransactionType",""),
                d.getOrDefault("vnp_TxnRef",""),
                d.getOrDefault("vnp_Amount",""),
                d.getOrDefault("vnp_TransactionNo",""),
                d.getOrDefault("vnp_TransactionDate",""),
                d.getOrDefault("vnp_CreateBy",""),
                d.getOrDefault("vnp_CreateDate",""),
                d.getOrDefault("vnp_IpAddr",""),
                d.getOrDefault("vnp_OrderInfo","")
        );
    }


//    @Override
//    public String handleVnpayIPN(HttpServletRequest request) {
//        try {
//            // 🔹 Lấy toàn bộ tham số từ request
//            Map<String, String> fields = new HashMap<>();
//            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
//                String fieldName = params.nextElement();
//                String fieldValue = request.getParameter(fieldName);
//                fields.put(fieldName, fieldValue);
//            }
//
//            // 🔹 Kiểm tra chữ ký
//            boolean validSignature = vnPayUtils.validateSignature(fields);
//            if (!validSignature) {
//                return "Invalid signature";
//            }
//
//            // 🔹 Xử lý kết quả giao dịch
//            String responseCode = fields.get("vnp_ResponseCode");
//            String txnRef = fields.get("vnp_TxnRef");
//
//            if ("00".equals(responseCode)) {
////                vnPayService.updateOrderStatus(txnRef, "PAID");
//                return "OK";
//            } else {
////                vnPayService.updateOrderStatus(txnRef, "FAILED");
//                return "FAILED";
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error: " + e.getMessage();
//        }
//    }

}
