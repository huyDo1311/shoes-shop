package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImp implements VerificationService {

    @Value("${jwt.expiration.refresh}")
    private Long refresh;

    private final RedisTemplate<String, String> redisTemplate;

    private String key(String email) {
        return "verify: " + email;
    }

    private String sessionKey(String email) {
        return "login_session: " + email.toLowerCase();
    }

    @Override
    public String generateAndStoreCode(String email) {
        String code = String.format("%06d", new Random().nextInt(1_000_000));

        redisTemplate.opsForValue().set(key(email), code, Duration.ofMinutes(5));
        return code;
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String redisKey = key(email);
        String storedCode = redisTemplate.opsForValue().get(redisKey);

        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(redisKey);
            return true;
        }

        return false;
    }

    @Override
    public boolean haveActiveSession(String email) {
        try {
            return redisTemplate.hasKey(sessionKey(email));
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void createOrUpdateSession(String email) {
        String e = email.toLowerCase();
        String sessionId = UUID.randomUUID().toString();

        try {
            redisTemplate.opsForValue().set(sessionKey(e), sessionId, Duration.ofSeconds(refresh));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot create login session at the moment. Please try again later.", ex);
        }
    }

    @Override
    public void clearSession(String email) {
        redisTemplate.delete(sessionKey(email));
    }

    @Override
    public boolean isValidSession(String email, String sessionId) {
        String e = email.toLowerCase();
        String current  = redisTemplate.opsForValue().get(sessionKey(e));
        if(current == null) {
            return false;
        }
        return current.equals(sessionId);
    }
}
