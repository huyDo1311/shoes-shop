package com.cybersoft.shop.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequest {
    @JsonProperty("amount")
    @NotNull(message = "The amount cannot be empty")
    @Min(value = 1, message = "The amount must be greater than 0")
    private Long amount; // Số tiền cần thanh toán

    @JsonProperty("bankCode")
    @Size(max = 20, message = "BankCode maximum 20 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9_]*$",
            message = "BankCode invalid"
    )

    @Pattern(
            regexp = "^(vn|en)$",
            message = "Language only accepts 'vn' or 'en'"
    )
    private String bankCode;

    @JsonProperty("language")
    private String language;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not in the correct format")
    @Size(max = 255, message = "Email maximum 255 characters")
    private String email;
}


