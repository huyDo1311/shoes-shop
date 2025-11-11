package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.config.LockoutConfig;
import com.cybersoft.shop.service.LockoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LockoutServiceImp implements LockoutService {

    private final RedisTemplate<String, String> redisTemplate;
    private final LockoutConfig lockout;

    private String failKey(String email) {
        return "login_fail: " + email;
    }

    private String cycleKey(String email) {
        return "login_cycle: " + email;
    }

    private String tempKey(String email) {
        return "login_lock: " + email;
    }

    private String permKey(String email) {
        return "login_permanent: " + email;
    }

    @Override
    public boolean isLocked(String email) {
        return redisTemplate.hasKey(tempKey(email));
    }

    @Override
    public boolean isPermanent(String email) {
        return redisTemplate.hasKey(permKey(email));
    }

    @Override
    public String getTimeRemaining(String email) {
        long ttlSeconds = redisTemplate.getExpire(tempKey(email), TimeUnit.SECONDS);

        if (ttlSeconds <= 0) {
            return "0 seconds";
        }

        long minutes = ttlSeconds / 60;
        long seconds = ttlSeconds % 60;

        if (minutes > 0) {
            return minutes + " minute" + (minutes > 1 ? "s" : "");
        } else {
            return seconds + " second" + (seconds > 1 ? "s" : "");
        }
    }

    @Override
    public String recordFailedAttempt(String email) {

        Long fail = redisTemplate.opsForValue().increment(failKey(email));
        if (fail == null) fail = 1L;

        if (fail == 1L) {
            redisTemplate.expire(failKey(email), Duration.ofMinutes(30));
        }

        if (fail < lockout.getAttemptsCycle()) {
            return "NONE";
        }

        redisTemplate.delete(failKey(email));

        Long cycles = redisTemplate.opsForValue().increment(cycleKey(email));
        if (cycles == null) cycles = 1L;

        redisTemplate.expire(cycleKey(email), Duration.ofDays(1));

        if (cycles >= lockout.getPermanentCycles()) {
            redisTemplate.opsForValue().set(permKey(email), email);
            redisTemplate.delete(tempKey(email));
            return "PERM_LOCKED";
        }

        redisTemplate.opsForValue().set(
                tempKey(email), email, Duration.ofMinutes(lockout.getLockDurationMinutes())
        );
        return "TEMP_LOCKED";
    }

    @Override
    public void onLoginSuccess(String email) {
        String e = email.toLowerCase();
        redisTemplate.delete(failKey(e));
        redisTemplate.delete(tempKey(e));
        redisTemplate.delete(cycleKey(e));
    }

    @Override
    public long getCurrentFail(String email) {
        String val = redisTemplate.opsForValue().get(failKey(email));
        if (val == null) return 0;
        try {
            return Long.parseLong(val);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
