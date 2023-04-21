package com.bonjour.practice.module.oauth.Service.Impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.bonjour.practice.common.dto.LoginDTO;
import com.bonjour.practice.common.entity.User;
import com.bonjour.practice.common.mapper.UserMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.AESUtil;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.module.oauth.Service.LoginService;
import jdk.internal.dynalink.linker.LinkerServices;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommonService commonService;

    @Override
    public User login(LoginDTO loginDTO) {
//        String password = loginDTO.getPassword();
        String password = AESUtil.encrypt(loginDTO.getPassword());
        User user = commonService.getMapper(UserMapper.class).query()
                .eq("password", password)
                .and(Wrapper -> Wrapper
                        .eq(StringUtils.isNotBlank(loginDTO.getNickName()), "nick_name", loginDTO.getNickName())
                        .or()
                        .eq(StringUtils.isNotBlank(loginDTO.getPhone()), "phone", loginDTO.getPhone())).one();
        if (user == null) {
            throw new RuntimeException("账号或密码错误！");
        }
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        redisUtil.setCacheObject(uuid, CommonUtils.beanToString(user));
        return user;
    }
}
