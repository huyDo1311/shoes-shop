package com.cybersoft.shop.service;

import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.request.SignInRequest;
import com.cybersoft.shop.request.SignUpRequest;
import com.cybersoft.shop.request.UserUpdateRequest;
import com.cybersoft.shop.response.user.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User signUp(SignUpRequest userDTO) throws Exception;
    String signIn(SignInRequest signInRequest) throws Exception;
    User findUserByEmail(SignInRequest signInRequest) throws Exception;

    UserResponse getInfo(String email);
    UserResponse updateUser(UserUpdateRequest request, MultipartFile avatar);
}
