package com.cybersoft.shop.service;

public interface LockoutService {

    boolean isLocked(String email);

    boolean isPermanent(String email);

    String getTimeRemaining(String email);

    String recordFailedAttempt(String email);

    void onLoginSuccess(String email);

    long getCurrentFail(String email);

    String failMessage(String ip);

    String lockMessageOrNull(String ip);
}

