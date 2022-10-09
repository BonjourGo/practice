package com.bonjour.practice.module.oauth.Service;

import com.bonjour.practice.common.dto.RegisterDTO;

/**
 * @authur tc
 * @date 2022/10/8 11:51
 */
public interface RegisterService {

    /**
     * 注册
     * @param user
     */
    void register(RegisterDTO user);
}
