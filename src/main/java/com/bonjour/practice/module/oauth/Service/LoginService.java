package com.bonjour.practice.module.oauth.Service;

import com.bonjour.practice.common.dto.LoginDTO;
import com.bonjour.practice.common.entity.User;

public interface LoginService {

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    User login(LoginDTO loginDTO);
}
