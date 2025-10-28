package com.cybersoft.shop.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String email;

    private String userName;
    private String dateOfBirth;
    private String address;
    private String phone;
    private String avatar;
}
