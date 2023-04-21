package com.bonjour.practice.common.interceptor;

import com.bonjour.practice.common.annotations.Limiter;
import com.bonjour.practice.common.utils.CommonUtils;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @authur tc
 * @date 2022/12/28 15:39
 * 限流interceptor
 */
@Slf4j
@Component
public class LimiterInterceptor implements HandlerInterceptor {

    private Map<String, RateLimiter> map = new ConcurrentHashMap<>();

    // ctrl + i Override 快捷键
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            log.info("请求地址{}", CommonUtils.getRequestIP(request) + request.getRequestURI());
            // 如果是请求上的方法
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Limiter limiter = handlerMethod.getMethodAnnotation(Limiter.class);
                if (limiter != null) {
                    // 接口添加了 Limiter 注解
                    // 获取请求地址
                    String url = request.getRequestURI();
                    RateLimiter rateLimiter;
                    // 如果map中不存在当前url则将当前url放入map中
                    if (!map.containsKey(url)) {
                        // 创建令牌桶
                        rateLimiter = RateLimiter.create(limiter.QPS());
                        map.put(url, rateLimiter);
                    }
                    rateLimiter = map.get(url);
                    // 获取令牌
                    boolean isLimit = rateLimiter.tryAcquire(limiter.timeOut(), limiter.timeUnit());
                    if (isLimit) {
                        // 放行
                        log.info(url + "被放行");
                        return true;
                    } else {
                        log.info(url + "被拦截");
                        throw new RuntimeException(limiter.msg());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("网络繁忙，请稍后重试！");
        }
        return true;
    }
}
