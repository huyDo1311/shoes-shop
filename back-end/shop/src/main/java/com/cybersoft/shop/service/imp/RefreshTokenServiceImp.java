package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.component.JwtTokenUtil;
import com.cybersoft.shop.entity.RefreshToken;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.repository.RefreshTokenRepository;
import com.cybersoft.shop.repository.UserRepository;
import com.cybersoft.shop.response.user.RefreshTokenResponse;
import com.cybersoft.shop.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImp implements RefreshTokenService {

    @Value("${jwt.expiration.refresh}")
    private long ACCESS_REFRESH_EXPIRATION;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public String createToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser(user);

        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(ACCESS_REFRESH_EXPIRATION);

        RefreshToken token;
        if (optionalRefreshToken.isPresent()) {
            // Cập nhật token cũ
            token = optionalRefreshToken.get();
            token.setRefreshToken(UUID.randomUUID().toString());
            token.setRefreshExpirationDate(expiryDate);
        } else {
            // Tạo token mới nếu chưa có
            token = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .refreshExpirationDate(expiryDate)
                    .user(user)
                    .build();
        }

        refreshTokenRepository.save(token);

        return token.getRefreshToken();
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenString)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getRefreshExpirationDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }

        User user = refreshToken.getUser();

        String newAccessToken = "Bearer " + jwtTokenUtil.generateAccessToken(user);

        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(ACCESS_REFRESH_EXPIRATION));
        refreshTokenRepository.save(refreshToken);

        RefreshTokenResponse response = RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();

        return response;
    }
}
