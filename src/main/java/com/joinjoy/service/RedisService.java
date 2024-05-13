package com.joinjoy.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private static final String EMAIL_VERIFICATION_ERROR_KEY_PREFIX = "email:verification:error:";
    private static final int MAX_EMAIL_VERIFICATION_ATTEMPTS = 3;
    private static final int EMAIL_VERIFICATION_LOCK_DURATION_MINUTES = 5;

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // set key-value
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // set key-value and time (per minute)
    public void set(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }

    // delete key
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // get value
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Long lockEmailVerification(String emailkey) {
        String key = EMAIL_VERIFICATION_ERROR_KEY_PREFIX + emailkey + ":locked";
        if (!redisTemplate.hasKey(key)
                || Integer.parseInt(redisTemplate.opsForValue().get(key)) < MAX_EMAIL_VERIFICATION_ATTEMPTS) {
            Long count = redisTemplate.opsForValue().increment(key, 1);
            redisTemplate.expire(key, EMAIL_VERIFICATION_LOCK_DURATION_MINUTES, TimeUnit.MINUTES);
            return count;
        } else {
            // 否则，返回 true 表示已被锁定
            return 0L;
        }
    }
}
