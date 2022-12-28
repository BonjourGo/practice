package com.bonjour.practice.common.annotations;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @authur tc
 * @date 2022/12/28
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Limiter {

    // 一次发放的令牌数
    int QPS() default 10;

    // 超时时间
    long timeOut() default 500;

    // 超时时间单位 毫秒
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    // msg
    String msg() default "操作太频繁了！";
}
