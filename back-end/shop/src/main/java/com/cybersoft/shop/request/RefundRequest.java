package com.cybersoft.shop.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequest {

    private String txnRef;
    private long amount;
    // 02 - hoàn toàn bộ, 03 - hoàn một phần
    private String transactionType;
    private String transactionDate;
    private String orderInfo;
    private String createBy;
}
