package com.bonjour.practice.common.annotations;

import java.lang.annotation.*;

/**
 * @authur tc
 * @date 2024/6/6 16:45
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatLimitV2 {
    // 多少时间内不能重复提交
    int timeOut() default 1;
}
