package com.bonjour.practice.module.common.service.impl;

import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.module.common.service.CommonControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @authur tc
 * @date 2022/10/13 16:01
 */
@Service
public class CommonControllerServiceImpl implements CommonControllerService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String getUUID() {
        String uuid = CommonUtils.getUUID();
        redisUtil.setCacheObjectAndExpire(uuid, uuid, 5, TimeUnit.SECONDS);
        return uuid;
    }
}
