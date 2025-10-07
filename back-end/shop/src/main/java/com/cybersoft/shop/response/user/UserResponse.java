package com.cybersoft.shop.response.user;

import com.cybersoft.shop.entity.Role;
import com.cybersoft.shop.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String email;

    private String userName;

    private String dateOfBirth;

    private String address;

    private String phone;

    private String avatar;

    private String role;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .userName(user.getUserName())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .role(user.getRole().getRoleName())
                .build();
    }
}
