package com.bonjour.practice.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 * @authur tc
 * @date 2022/10/8 16:45
 */
@Slf4j
@Component
public class RedisUtil {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    @Autowired
    private static RedisTemplate redisTemplate;

//    @Autowired
//    private JedisPool jedisPool;

    public RedisUtil(RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    /**
     * 加锁
     * @param key
     * @param value
     * @return
     */
    public static boolean setNx(String key, String value) {
        Object object = null;
        try {
            object = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
                    Boolean success = connection.setNX(serializer.serialize(key), serializer.serialize(value));
                    connection.close();
                    return success;
                }
            });
        } catch (Exception e) {
            log.error("Redis 加锁失败 ：", e.toString());
        }
        return object != null ? (boolean) object : false;
    }

    /**
     * 释放锁
     * @param key
     * @return
     */
    public static boolean removeNx(String key) {
        Object object = null;
        try {
            object = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
                    Boolean success = connection.del(serializer.serialize(key)).longValue() > 0 ? true : false;
                    connection.close();
                    return success;
                }
            });
        } catch (Exception e) {
            log.error("Redis 释放锁失败 ：", e.toString());
        }
        return object != null ? (boolean) object : false;
    }

    /**
     *  redis 获得自增id 有前缀
     * @param keyPrefix
     * @return
     */
    public String getIncrId(String keyPrefix) {
        // 格式化时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String formatDate = simpleDateFormat.format(date);
        // 获取自增
        RedisAtomicLong redisAtomicLong = new RedisAtomicLong(formatDate, redisTemplate.getConnectionFactory());
        Long incr = redisAtomicLong.incrementAndGet();
        // 自增起始号码
        DecimalFormat decimalFormat = new DecimalFormat("0000000000");
        String value = decimalFormat.format(incr);
        String id = keyPrefix + formatDate + value;
        return id;
    }

    /**
     *  redis 获得key一定格式自增id
     * @param
     * @return
     */
    public String getIncrIdString(String key) {
        // 格式化时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        String formatDate = simpleDateFormat.format(date);
        // 获取自增
        Long incr = (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] keys = serializer.serialize("sequence:id_" + key);
            return connection.incr(keys);
        } );
        // 自增起始号码
        DecimalFormat decimalFormat = new DecimalFormat("0000000000");
        String value = decimalFormat.format(incr);
        String id = formatDate + value;
        return id;
    }


    /**
     *  redis 获得key一定格式自增id 自定义日期格式
     * @param
     * @return
     */
    public String getIncrIdString(String key, String pattern) {
        // 格式化时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        String formatDate = simpleDateFormat.format(date);
        // 获取自增
        Long incr = (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] keys = serializer.serialize("sequence:id_" + key);
            return connection.incr(keys);
        } );
        // 自增起始号码
        DecimalFormat decimalFormat = new DecimalFormat("0000000000");
        String value = decimalFormat.format(incr);
        String id = formatDate + value;
        return id;
    }

    /**
     * 设置redis键值对
     *
     * @param key   redis键
     * @param value redis值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置redis键值对
     *
     * @param key      redis键
     * @param value    redis值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public <T> void setCacheObjectAndExpire(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获取Redis缓存
     * @param key
     * @param <T>
     * @return
     */
    public <T> Object getCacheObject(final String key) {
        Object result = redisTemplate.opsForValue().get(key);
        return result;
    }

    public boolean isNull(String key) {
        String b = redisTemplate.opsForValue().get(key).toString();
        if (StringUtils.isBlank(b)) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 获取自增值从1开始 无前缀
     * @param key
     * @return
     */
    public Long getIncrLongId(String key) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] keys = serializer.serialize("sequence:id_" + key);
            return connection.incr(keys);
        } );
    }

    /**
     * 删除单个对象
     *
     * @param key redis键
     * @return boolean
     */
    public static boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }
}
