package com.bonjour.practice.module.oauth.Service.Impl;

import com.bonjour.practice.common.dto.RegisterDTO;
import com.bonjour.practice.common.entity.User;
import com.bonjour.practice.common.mapper.UserMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.AESUtil;
import com.bonjour.practice.module.oauth.Service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

/**
 * @authur tc
 * @date 2022/10/8 11:51
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private CommonService commonService;

    @Override
    public void register(RegisterDTO dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        if (StringUtils.isBlank(user.getPhone())) {
            throw new RuntimeException("请输入手机号！");
        }
        if (StringUtils.isBlank(user.getPassword())) {

        }
        if (user.getPhone().length() < 11) {
            throw new RuntimeException("请输入正确的手机号！");
        }
        if (user.getPassword().length() < 6) {
            throw new RuntimeException("密码太短啦！");
        }
        if (commonService.getMapper(UserMapper.class).query().eq("phone", user.getPhone()).count() > 0) {
            throw new RuntimeException("该手机号已注册，请登录！");
        }
        user.setPassword(AESUtil.encrypt(user.getPassword()));
        user.setId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase(Locale.ROOT));
        user.setStatus("");
        commonService.insert(user, UserMapper.class);
    }
}
