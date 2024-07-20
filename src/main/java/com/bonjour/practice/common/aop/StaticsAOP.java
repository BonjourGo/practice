package com.bonjour.practice.common.aop;

import com.bonjour.practice.common.annotations.RepeatLimitV2;
import com.bonjour.practice.common.annotations.Statics;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @authur tc
 * @date 2024/7/5 13:16
 */
@Slf4j
@Aspect
@Component
public class StaticsAOP {

    @Pointcut("@annotation(statics)")
    public void pointCut(Statics statics) {
        //切入点不需要代码
        System.out.println("===================");
    }

    @Around("pointCut(statics)")
    public Object around(ProceedingJoinPoint pjp, Statics statics) throws Throwable {
        Object result = null;
        // 执行进程
//        long s = System.currentTimeMillis();
//        result = pjp.proceed();
//        log.info("result = {}", result);
//        long s1 = System.currentTimeMillis();
//        long use = (s1 - s) / 1000;
//        log.info("use : {}s", use);
        try {
            // 执行进程
            long s = System.currentTimeMillis();
            result = pjp.proceed();
            log.info("result = {}", result);
            long s1 = System.currentTimeMillis();
            long use = (s1 - s) / 1000;
            log.info("use : {}s", use);
        } catch (Exception e) {
            result = Result.error(e.toString());
            log.error("result error = {}", e.getMessage());
        }
        return result;
    }
}
