package com.bonjour.practice.module.common.controller;

import com.bonjour.practice.common.utils.Result;
import com.bonjour.practice.module.common.service.CommonControllerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @authur tc
 * @date 2022/10/13 15:59
 */
@Api(tags = "common controller")
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonControllerService commonControllerService;

    @GetMapping("/getUUID")
    @ApiOperation("get common uuid")
    public Result getUUID() {
        String uuid = commonControllerService.getUUID();
        return Result.ok(uuid);
    }
 }
