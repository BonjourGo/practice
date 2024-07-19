package com.bonjour.practice.common.aop;

import com.bonjour.practice.common.annotations.RepeatLimitV2;
import com.bonjour.practice.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @authur tc
 * @date 2024/6/6 16:46
 */
@Slf4j
@Aspect
@Component
public class RepeatLimitV2AOP {

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(repeatLimitV2)")
    public void pointCut(RepeatLimitV2 repeatLimitV2) {
        //切入点不需要代码
        System.out.println("===================");
    }

    @Around("pointCut(repeatLimitV2)")
    public Object around(ProceedingJoinPoint pjp, RepeatLimitV2 repeatLimitV2) throws Throwable {
        int lockSeconds = repeatLimitV2.timeOut();
        String key = getKey();
        log.info("lockKey:{}", key);
        String lockValue = getLockValue();
//        RLock lock = redissonClient.getLock(key);
        boolean isLock = RedisUtil.tryLock(key, lockValue, (long) lockSeconds);
        if (isLock) {
            // 获取锁成功
            Object result;
            try {
                // 执行进程
                result = pjp.proceed();
            } finally {
                // 解锁
                RedisUtil.releaseLock(key, lockValue);
            }
            return result;
        } else {
            throw new RuntimeException("重复请求，请稍后再试");
        }
    }

    private String getKey() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
        Assert.notNull(request, "request can not null");
        // 此处可以用token或者JSessionId
        String token = request.getHeader("Authorization");
        String path = request.getServletPath();
        return token + path;
    }

    private String getLockValue() {
        return UUID.randomUUID().toString();
    }
}
