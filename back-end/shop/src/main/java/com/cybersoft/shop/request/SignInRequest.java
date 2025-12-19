package com.cybersoft.shop.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SignInRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not in the correct format")
    @Size(max = 255, message = "Email maximum 255 characters")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(max = 100, message = "Invalid password")
    private String password;
}
