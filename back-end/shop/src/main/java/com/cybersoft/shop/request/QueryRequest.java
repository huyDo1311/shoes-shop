package com.cybersoft.shop.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {
    @NotBlank(message = "txnRef is required")
    @Size(max = 50, message = "txnRef maximum 50 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9_-]*$",
            message = "txnRef invalid"
    )
    private String txnRef;

    @NotBlank(message = "transactionDate is required")
    @Pattern(
            regexp = "^\\d{14}$",
            message = "transactionDate must be in the format yyyyMMddHHmmss"
    )
    private String transactionDate;

    @Size(max = 50, message = "transactionNo maximum 50 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9_-]*$",
            message = "transactionNo invalid"
    )
    private String transactionNo;

    @Size(max = 255, message = "orderInfo maximum 255 characters")
    private String orderInfo;


}
