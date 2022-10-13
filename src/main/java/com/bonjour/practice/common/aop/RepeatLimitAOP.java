package com.bonjour.practice.common.aop;

import com.bonjour.practice.common.annotations.RepeatLimit;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 重复提交aop代码
 * @authur tc
 * @date 2022/10/13 16:41
 */
@Slf4j
@Aspect
@Component
public class RepeatLimitAOP {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.bonjour.practice.common.annotations.RepeatLimit)")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        // 获取request
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        // 获取请求ip
        String ip = CommonUtils.getRequestIP(request);
        if (StringUtils.isBlank(ip)) {
            throw new RuntimeException("非法操作!");
        }
        // 获取请求路径
        String path = request.getServletPath();
        // 完整请求路径
        String requestPath = ip + path;
        log.info(requestPath);
        // 获取uuid
        String uuid = CommonUtils.getUUID();
        // 从redis获取值
        String value = (String) redisUtil.getCacheObject(requestPath);
        // 获取注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RepeatLimit repeatLimit = method.getAnnotation(RepeatLimit.class);
        Integer time = repeatLimit.timeOut();
        if (StringUtils.isBlank(value)) {
            // 不存在值代表没有请求过 放行
            redisUtil.setCacheObjectAndExpire(requestPath, uuid, time, TimeUnit.SECONDS);
        } else {
            // 存在值代表已经请求过 不放行
            throw new RuntimeException("请勿重复提交！");
        }
    }
}
