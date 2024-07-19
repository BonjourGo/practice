package com.bonjour.practice.common.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @authur tc
 * @date 2024/5/27 14:18
 */
@Data
@Component
@ConfigurationProperties(prefix = "doublecache")
public class CaffeineProperties {
    private Boolean allowNull = true;
    private Integer init = 100;
    private Integer max = 1000;
    private Long expireAfterWrite ;
    private Long expireAfterAccess;
    private Long refreshAfterWrite;
    private Long redisExpire;
}
