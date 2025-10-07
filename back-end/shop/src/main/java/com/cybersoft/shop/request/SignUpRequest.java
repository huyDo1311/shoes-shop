package com.cybersoft.shop.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
}
