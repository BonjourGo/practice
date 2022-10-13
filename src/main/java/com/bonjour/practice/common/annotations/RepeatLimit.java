package com.bonjour.practice.common.annotations;

import java.lang.annotation.*;

/**
 * 重复提交注解
 * @authur tc
 * @date 2022/10/13 16:38
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatLimit {

    // 多少时间内不能重复提交
    int timeOut() default 1;
}
