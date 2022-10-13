package com.bonjour.practice.module.common.service;

/**
 * @authur tc
 * @date 2022/10/13 16:00
 */
public interface CommonControllerService {

    /**
     * 获取uuid并存入redis
     * @return
     */
    String getUUID();
}
