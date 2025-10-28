package com.cybersoft.shop.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserInfoRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;
}
