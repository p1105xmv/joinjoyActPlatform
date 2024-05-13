package com.joinjoy.security;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

public class EmailVerificationService {
    private static final String EMAIL_VERIFICATION_ERROR_KEY_PREFIX = "email:verification:error:";
    private static final int MAX_EMAIL_VERIFICATION_ATTEMPTS = 3;
    private static final int EMAIL_VERIFICATION_LOCK_DURATION_MINUTES = 5;

    private final RedisTemplate<String, Integer> redisTemplate;

    public EmailVerificationService(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void verifyEmail(String email, String verificationCode) {
        // 檢查信箱是否已被鎖定
        if (isEmailVerificationLocked(email)) {
            throw new RuntimeException("邮箱验证已被锁定");
        }

        // 檢查是否超過限制
        boolean isValid = validateEmailAndCode(email, verificationCode);

        if (!isValid) {
            // 增加驗證錯誤次数
            incrementEmailVerificationAttempts(email);

            // 检查是否超過限制
            if (isEmailVerificationAttemptsExceeded(email)) {
                // 鎖定信箱
                lockEmailVerification(email);
                throw new RuntimeException("信箱驗證錯誤次數過多，請稍後再試");
            } else {
                throw new RuntimeException("信箱驗證錯誤");
            }
        } else {
            // 驗證成功，重置驗證錯誤次數
            resetEmailVerificationAttempts(email);
        }
    }

    private boolean validateEmailAndCode(String email, String verificationCode) {
        return true;
    }

    private void incrementEmailVerificationAttempts(String email) {
        String key = EMAIL_VERIFICATION_ERROR_KEY_PREFIX + email;
        redisTemplate.opsForValue().increment(key, 1);
        // 重置過期時間
        redisTemplate.expire(key, EMAIL_VERIFICATION_LOCK_DURATION_MINUTES, TimeUnit.MINUTES);
    }

    // 檢查信箱驗證次數是否超過限制
    private boolean isEmailVerificationAttemptsExceeded(String email) {
        String key = EMAIL_VERIFICATION_ERROR_KEY_PREFIX + email;
        Integer verificationAttempts = redisTemplate.opsForValue().get(key);
        return verificationAttempts != null && verificationAttempts >= MAX_EMAIL_VERIFICATION_ATTEMPTS;
    }

    private void lockEmailVerification(String email) {
        String key = EMAIL_VERIFICATION_ERROR_KEY_PREFIX + email + ":locked";
        redisTemplate.opsForValue().set(key, 1, EMAIL_VERIFICATION_LOCK_DURATION_MINUTES, TimeUnit.MINUTES);
    }

    // 檢查信箱是否存在
    private boolean isEmailVerificationLocked(String email) {
        String key = EMAIL_VERIFICATION_ERROR_KEY_PREFIX + email + ":locked";
        return redisTemplate.hasKey(key);
    }

    private void resetEmailVerificationAttempts(String email) {
        String key = EMAIL_VERIFICATION_ERROR_KEY_PREFIX + email;
        redisTemplate.delete(key);
    }
}
