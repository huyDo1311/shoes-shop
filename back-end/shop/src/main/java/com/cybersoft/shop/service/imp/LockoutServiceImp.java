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

    private String normIp(String ip) {
        return ip == null ? "" : ip.trim();
    }

    private String failKeyIp(String ip) { return "login_fail: " + normIp(ip); }
    private String cycleKeyIp(String ip) { return "login_cycle: " + normIp(ip); }
    private String tempKeyIp(String ip) { return "login_lock: " + normIp(ip); }
    private String permKeyIp(String ip) { return "login_permanent: " + normIp(ip); }

    @Override
    public boolean isLocked(String ip) {
        return redisTemplate.hasKey(tempKeyIp(ip));
    }

    @Override
    public boolean isPermanent(String ip) {
        return redisTemplate.hasKey(permKeyIp(ip));
    }

    @Override
    public String getTimeRemaining(String ip) {
        long ttlSeconds = redisTemplate.getExpire(tempKeyIp(ip), TimeUnit.SECONDS);

        if (ttlSeconds == -2) return "Not locked";
        if (ttlSeconds == -1) return "Unknown (no TTL)";

        long minutes = ttlSeconds / 60;
        long seconds = ttlSeconds % 60;

        if (minutes > 0) return minutes + " minute" + (minutes > 1 ? "s" : "");
        return seconds + " second" + (seconds > 1 ? "s" : "");
    }

    @Override
    public String recordFailedAttempt(String ip) {

        String keyFail = failKeyIp(ip);
        String keyCycle = cycleKeyIp(ip);
        String keyTemp = tempKeyIp(ip);
        String keyPerm = permKeyIp(ip);

        Long fail = redisTemplate.opsForValue().increment(keyFail);
        if (fail == null) fail = 1L;

        if (fail == 1L) {
            redisTemplate.expire(keyFail, Duration.ofMinutes(30));
        }

        if (fail < lockout.getAttemptsCycle()) {
            return "NONE";
        }

        // đạt attempts-cycle => reset fail, tăng cycle
        redisTemplate.delete(keyFail);

        Long cycles = redisTemplate.opsForValue().increment(keyCycle);
        if (cycles == null) cycles = 1L;

        redisTemplate.expire(keyCycle, Duration.ofDays(1));

        if (cycles >= lockout.getPermanentCycles()) {
            redisTemplate.opsForValue().set(keyPerm, "1"); // perm lock IP
            redisTemplate.delete(keyTemp);
            redisTemplate.delete(keyCycle);
            return "PERM_LOCKED";
        }

        redisTemplate.opsForValue().set(
                keyTemp, "1", Duration.ofMinutes(lockout.getLockDurationMinutes())
        );
        return "TEMP_LOCKED";
    }

    @Override
    public void onLoginSuccess(String ip) {
        redisTemplate.delete(failKeyIp(ip));
        redisTemplate.delete(tempKeyIp(ip));
        redisTemplate.delete(cycleKeyIp(ip));
    }

    @Override
    public long getCurrentFail(String ip) {
        String val = redisTemplate.opsForValue().get(failKeyIp(ip));
        if (val == null) return 0;
        try { return Long.parseLong(val); }
        catch (NumberFormatException ex) { return 0; }
    }

    @Override
    public String failMessage(String ip) {
        String status = recordFailedAttempt(ip);

        if ("PERM_LOCKED".equals(status)) {
            return "Too many failed attempts. Access temporarily restricted.";
        }

        if ("TEMP_LOCKED".equals(status)) {
            return "Too many failed attempts. Try again in "
                    + lockout.getLockDurationMinutes() + " minutes.";
        }

        return "Invalid email or password ("
                + getCurrentFail(ip) + "/"
                + lockout.getAttemptsCycle() + ").";
    }

    @Override
    public String lockMessageOrNull(String ip) {
        if (isPermanent(ip)) {
            return "Too many failed attempts. Access temporarily restricted.";
        }
        if (isLocked(ip)) {
            return "Too many failed attempts. Try again in " + getTimeRemaining(ip);
        }
        return null;
    }

}

