package com.project.harupuppy.domain.user.application;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValueWithDuration(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public Optional<String> getValues(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    public void deleteValuesByUserId(String userId) {
        Set<String> keys = redisTemplate.keys(userId + "_*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}