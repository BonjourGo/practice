package com.bonjour.practice.common.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.Callable;

/**
 * @authur tc
 * @date 2024/5/27 11:08
 */
@Slf4j
public class MyCache extends AbstractValueAdaptingCache {

    private String cacheKey;

    private Cache<Object, Object> caffeine;

    private RedisTemplate<Object, Object> redisTemplate;

    private CaffeineProperties caffeineProperties;

    protected MyCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    public MyCache(String cacheKey,
                   Cache<Object, Object> caffeine,
                   RedisTemplate<Object, Object> redisTemplate,
                   CaffeineProperties caffeineProperties) {
        super(caffeineProperties.getAllowNull());
        this.cacheKey = cacheKey;
        this.caffeine = caffeine;
        this.redisTemplate = redisTemplate;
        this.caffeineProperties = caffeineProperties;
    }

    @Override
    protected Object lookup(Object key) {
        Object result = caffeine.getIfPresent(key);
        if (result != null) {
            log.info("从caffeine获取{}值{}", key, result);
            return result;
        }
        result = redisTemplate.opsForValue().get(key);
        if (result != null) {
            log.info("从redis获取{}值{}", key, result);
            return result;
        }
        return result;
    }

    @Override
    public String getName() {
        return this.cacheKey;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        return null;
    }

    @Override
    public void put(Object o, Object o1) {

    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        return null;
    }

    @Override
    public void evict(Object o) {

    }

    @Override
    public void clear() {

    }
}
