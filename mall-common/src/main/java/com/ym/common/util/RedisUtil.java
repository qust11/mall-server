package com.ym.common.util;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author qushutao
 * @since 2026-06-16 21:28
 **/
@Component
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setNx(String key, Object value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public void expireSecond(String key, long timeout) {
        expire(key, timeout, TimeUnit.SECONDS);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public long increment(String key) {
        return incrementN(key, 1);
    }

    public long incrementN(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    public long decrement(String key) {
        return decrementN(key, 1);
    }

    public long decrementN(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }
}
