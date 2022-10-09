package com.bonjour.practice.module.order.controller;

import com.bonjour.practice.common.entity.Order;
import com.bonjour.practice.common.utils.Result;
import com.bonjour.practice.module.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @authur tc
 * @date 2022/10/9 15:43
 */
@Api(tags = "下单controller")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    @ApiOperation("下单")
    public Result order(@RequestBody Order order) {
        orderService.order(order);
        return Result.ok();
    }

}
