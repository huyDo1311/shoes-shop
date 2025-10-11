package com.cybersoft.shop.service;

import com.cybersoft.shop.entity.RefreshToken;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.response.user.RefreshTokenResponse;

import java.util.Map;

public interface RefreshTokenService {
    String createToken(String email);
    RefreshTokenResponse refreshToken(String refreshToken);
}
