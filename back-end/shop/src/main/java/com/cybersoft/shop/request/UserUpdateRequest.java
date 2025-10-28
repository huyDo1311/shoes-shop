package com.cybersoft.shop.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 100)
    private String userName;

    @Size(max = 20)
    private String dateOfBirth;

    @Size(max = 255)
    private String address;

    @Size(max = 20)
    private String phone;

    private String avatar;
}
