package com.cybersoft.shop.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {
    private String txnRef;
    private String transactionDate;
    private String transactionNo;
    private String orderInfo;
}
