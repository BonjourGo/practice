package com.bonjour.practice.common.annotations;

import java.lang.annotation.*;

/**
 * @authur tc
 * @date 2024/7/5 13:15
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Statics {
}
