package com.bonjour.practice.module.redpacket.controller;

import com.bonjour.practice.common.entity.RedPacket;
import com.bonjour.practice.common.utils.Result;
import com.bonjour.practice.module.redpacket.service.RedPacketService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @authur tc
 * @date 2023/2/7 10:11
 */
@RestController
@RequestMapping("/redPacket")
public class RedPacketController {

    @Autowired
    private RedPacketService redPacketService;

    @ApiOperation("发红包")
    @PostMapping("/sendRedPacket")
    public Result sendRedPacket(@RequestBody RedPacket redPacket) {
        redPacketService.sendRedPacket(redPacket);
        return Result.ok();
    }

    @ApiOperation("抢红包")
    @PostMapping("/getPacket")
    public Result getPacket(String userId, String packetId) {
        BigDecimal bigDecimal = redPacketService.getPacket(userId, packetId);
        return Result.ok(bigDecimal);
    }
}
