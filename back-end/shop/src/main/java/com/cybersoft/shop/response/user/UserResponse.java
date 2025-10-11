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

    private List<String> roles;

    public static UserResponse toDTOUser(User user) {
        List<String> roleNames = new ArrayList<>();

        if (user.getUserRoles() != null) {
            roleNames = user.getUserRoles()
                    .stream()
                    .map(userRole -> userRole.getRole().getRoleName())
                    .toList();
        }
        return UserResponse.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .roles(roleNames)
                .build();
    }
}
