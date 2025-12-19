package com.cybersoft.shop.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VNPStatusUpdateRequest {

    @NotBlank(message = "vnp_TxnRef cannot be empty")
    @Size(max = 50, message = "vnp_TxnRef maximum 50 characters")
    private String vnp_TxnRef;
}
