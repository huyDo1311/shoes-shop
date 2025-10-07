package com.cybersoft.shop.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
