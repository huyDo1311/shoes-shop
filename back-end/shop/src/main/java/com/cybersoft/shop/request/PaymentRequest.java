package com.cybersoft.shop.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequest {
    @JsonProperty("amount")
    private Long amount; // Số tiền cần thanh toán

    @JsonProperty("bankCode")
    private String bankCode;

    @JsonProperty("language")
    private String language;

    private String email;
}


