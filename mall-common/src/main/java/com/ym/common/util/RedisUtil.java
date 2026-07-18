package com.ym.common.util;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    public <T> T getHash(String key, String hashKey, Class<T> clazz) {
        Object o = redisTemplate.opsForHash().get(key, hashKey);
        if (null == o) {
            return null;
        }
        return JSON.parseObject(o.toString(), clazz);
    }

    public <T> List<T> getHashAllMember(String key, Class<T> clazz) {
        Map<Object, Object> hashMap = redisTemplate.opsForHash().entries(key);
        List< T> list = new ArrayList<>();
        hashMap.forEach((k,v)-> list.add(JSON.parseObject(v.toString(), clazz)));
        return list;
    }

    public Long getHashAllMemberCount(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    public void putHash(String key, String hashKey, String str, long ttlSeconds) {
        redisTemplate.opsForHash().put(key, hashKey, str);
        redisTemplate.opsForHash().expire(key, Duration.ofSeconds(ttlSeconds), List.of(hashKey));
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
