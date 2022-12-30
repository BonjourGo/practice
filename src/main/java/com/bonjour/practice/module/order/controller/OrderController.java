package com.bonjour.practice.module.order.controller;

import com.bonjour.practice.common.annotations.Limiter;
import com.bonjour.practice.common.annotations.RepeatLimit;
import com.bonjour.practice.common.entity.Order;
import com.bonjour.practice.common.utils.Result;
import com.bonjour.practice.module.order.service.OrderService;
import com.google.common.util.concurrent.RateLimiter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @authur tc
 * @date 2022/10/9 15:43
 */
@Slf4j
@Api(tags = "下单controller")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private RateLimiter rateLimiter = RateLimiter.create(10);

    @RepeatLimit(timeOut = 5)
    @PostMapping("/order")
    @ApiOperation("下单")
    public Result order(@RequestBody Order order) {
        orderService.order(order);
        return Result.ok();
    }

    @ApiOperation("抢购")
    @PostMapping("/orderSpecial")
    @Limiter
    public Result orderSpecial(String phone, Long productId) {
        orderService.orderSpecial(phone, productId);
        return Result.ok();
    }

    @ApiOperation("抢购-redisson")
    @PostMapping("/orderRedisson")
    public Result orderRedisson(String phone, Long productId) {
        final double isLimit = rateLimiter.acquire();
        if (isLimit == 0) {
            orderService.orderForRedisson(phone, productId);
        } else {
            throw new RuntimeException("操作太频繁了！");
        }
        return Result.ok();
    }

    public void test() {
        final double isLimit = rateLimiter.acquire();
        if (isLimit == 0) {
            log.info("-----success-----");
        } else {
            log.info("-----fail-----");
        }
    }

    @Limiter
    public void test01() {
        log.info("aaa");
    }

    @ApiOperation("加载商品")
    @PostMapping("/init")
    public Result init() {
        orderService.init();
        return Result.ok();
    }

}
