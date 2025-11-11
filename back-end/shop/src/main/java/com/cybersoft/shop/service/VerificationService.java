package com.cybersoft.shop.service;

public interface VerificationService {
    String generateAndStoreCode(String email);

    boolean verifyCode(String email, String code);

    boolean haveActiveSession(String email);

    void createOrUpdateSession(String email);

    void clearSession(String email);

    boolean isValidSession(String email, String sessionId);
}
