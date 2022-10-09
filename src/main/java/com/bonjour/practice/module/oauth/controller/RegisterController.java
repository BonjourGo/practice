package com.bonjour.practice.module.oauth.controller;

import com.bonjour.practice.common.dto.RegisterDTO;
import com.bonjour.practice.common.utils.Result;
import com.bonjour.practice.module.oauth.Service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@Api(tags = "注册API")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @ApiOperation("注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO user) {
        registerService.register(user);
        return Result.ok();
    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody RegisterDTO user) {
        registerService.register(user);
        return Result.ok();
    }
}
