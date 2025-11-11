package com.cybersoft.shop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class LockoutConfig {
    @Value("${lockout.attempts-cycle}")
    private int attemptsCycle;
    @Value("${lockout.temporary-lock}")
    private int lockDurationMinutes;
    @Value("${lockout.permanent-cycles}")
    private int permanentCycles;
}
