package com.cybersoft.shop.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequest {

    @NotBlank(message = "txnRef is required")
    @Size(max = 50, message = "txnRef maximum 50 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9_-]*$",
            message = "txnRef invalid"
    )
    private String txnRef;

    @NotNull(message = "The amount cannot be empty")
    @Min(value = 1, message = "The amount must be greater than 0")
    private long amount;
    // 02 - hoàn toàn bộ, 03 - hoàn một phần

    @NotBlank(message = "transactionType is required")
    @Pattern(
            regexp = "^(02|03)$",
            message = "transactionType only accepts 02 (full) or 03 (partial)"
    )
    private String transactionType;
    @NotBlank(message = "transactionDate is required")
    @Pattern(
            regexp = "^\\d{14}$",
            message = "transactionDate must be in the format yyyyMMddHHmmss"
    )
    private String transactionDate;
    @Size(max = 255, message = "orderInfo maximum 255 characters")
    private String orderInfo;
    private String createBy;
}
