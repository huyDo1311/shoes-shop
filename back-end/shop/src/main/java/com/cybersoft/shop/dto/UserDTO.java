package com.cybersoft.shop.dto;

import com.cybersoft.shop.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String email;

    private String userName;

    private String password;

    private Date dateOfBirth;

    private String address;

    private String phone;

    private String avatar;

    private Role role;
}
