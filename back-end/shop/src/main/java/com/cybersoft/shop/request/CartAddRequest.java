package com.cybersoft.shop.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartAddRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String sku;
    @Min(1)
    private int quantity;

}
