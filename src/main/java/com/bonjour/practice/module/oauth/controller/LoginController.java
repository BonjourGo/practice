package com.bonjour.practice.module.oauth.controller;

import com.bonjour.practice.common.dto.LoginDTO;
import com.bonjour.practice.common.entity.User;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.CookieUtil;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.common.utils.Result;
import com.bonjour.practice.module.oauth.service.LoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private LoginService loginService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        User user = loginService.login(loginDTO);
        String uuid = CommonUtils.getUUID();
        if (user != null) {
            redisUtil.setCacheObjectAndExpire(uuid, CommonUtils.beanToString(user), 60 * 30, TimeUnit.SECONDS);
            cookieUtil.addCookie(response, "token", uuid, 60 * 30);
        }
        return Result.ok(user);
    }
}
