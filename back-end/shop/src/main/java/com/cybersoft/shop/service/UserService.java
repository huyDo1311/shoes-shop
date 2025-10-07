package com.cybersoft.shop.service;

import com.cybersoft.shop.dto.UserDTO;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.request.SignInRequest;
import com.cybersoft.shop.request.SignUpRequest;

public interface UserService {
    User signUp(SignUpRequest userDTO) throws Exception;
    String signIn(SignInRequest signInRequest) throws Exception;
    User findUserByEmail(SignInRequest signInRequest) throws Exception;
}
