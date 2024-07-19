package com.bonjour.practice.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.channels.Selector;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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

    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

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

    public <T> void setForHash(final String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public <T> Object getKeyForHash(final String key) {
        Object object = redisTemplate.opsForHash().entries(key);
        return object;
    }

    public <T> Object getKeyForHashField(final String key, String fieldName) {
        Object object = redisTemplate.opsForHash().get(key, fieldName);
        return object;
    }

    public <T> void setKeyLeftForList(final String key, String value) {
        long l = redisTemplate.opsForList().leftPush(key, value);
        System.out.println(l);
    }

    public List getIndex(String key) {
        List list = redisTemplate.opsForList().range(key, 0, -1);
        return list;
    }

    public <T> Object getKeyRightForList(final String key) {
        Object object = redisTemplate.opsForList().rightPop(key);
        return object;
    }
    //
    public void removeKeyForList(String key, String value) {
        redisTemplate.opsForList().remove(key, 0, value);
    }

    public boolean isNull(String key) {
        return redisTemplate.hasKey(key);
    }

    public void setKeyExpire(String key, Long time) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }


    /**
     * 获取自增值从1开始 无前缀
     * @param key
     * @return
     */
    public static Long getIncrLongId(String key) {
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
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    public Long descStock(Long productId) {
        String id = "id_" + productId;
//        System.out.println(this.getCacheObject(id));
        String script = "local isExist = redis.call('exists', KEYS[1]) if isExist == 1 then local goodsNumber = redis.call('get', KEYS[1]) if goodsNumber > \"0\" then redis.call('decr', KEYS[1]) return 1 else redis.call('del', KEYS[1]) return 0 end else return 2 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = (Long) redisTemplate.execute(redisScript, Arrays.asList(id));
        return result;
    }

    public void decrId(String id) {
        redisTemplate.opsForValue().decrement(id);
    }


    /**
     * -- 判断商品是否存在
     * local isExist = redis.call('exists', KEYS[1])                 -- 判断商品是否存在
     *     if isExist == 1 then
     *         local goodsNumber = redis.call('get', KEYS[1])        -- 获取商品的数量
     *         if goodsNumber > "0" then
     *             redis.call('decr', KEYS[1])                       -- 如果商品数量大于0，则库存减1
     *             return "success"
     *         else
     *             redis.call("del", KEYS[1])                        -- 商品数量为0则从秒杀活动删除该商品
     *             return "fail"
     *         end
     *     else return "notfound"
     * end
     */

    public static void stringJug(String s, String message) {
        if (StringUtils.isBlank(s)) {
            throw new RuntimeException(message);
        }
    }

    public void setValue(T t) {

    }

    public static Boolean tryLock(String key, String value, Long seconds) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, seconds, TimeUnit.SECONDS));
        } catch (Exception e) {
            log.error("获取redis锁异常", e);
            return false;
        }
    }

    public static boolean releaseLock(String lockKey, String lockValue) {
        try {
            RedisScript<Boolean> redisScript = RedisScript.of(RELEASE_LOCK_SCRIPT, Boolean.class);
            Boolean result = (Boolean) redisTemplate.execute(redisScript, Collections.singletonList(lockKey), lockValue);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("删除redis锁异常", e);
            return false;
        }
    }
}
