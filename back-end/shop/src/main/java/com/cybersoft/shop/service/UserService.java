package com.cybersoft.shop.service;

import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.request.SignInRequest;
import com.cybersoft.shop.request.SignUpRequest;
import com.cybersoft.shop.response.user.UserResponse;

public interface UserService {
    User signUp(SignUpRequest userDTO) throws Exception;
    String signIn(SignInRequest signInRequest) throws Exception;
    User findUserByEmail(SignInRequest signInRequest) throws Exception;

    UserResponse getInfo(String email);
}
