package com.bonjour.practice.module.redpacket.service;

import com.bonjour.practice.common.entity.RedPacket;

import java.math.BigDecimal;

/**
 * @authur tc
 * @date 2023/2/7 10:17
 */
public interface RedPacketService {

    /**
     * 发红包
     * @param redPacket
     */
    void sendRedPacket(RedPacket redPacket);

    /**
     * 抢红包
     * @param userId
     * @param packetId
     */
    BigDecimal getPacket(String userId, String packetId);
}
