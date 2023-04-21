package com.bonjour.practice.module.redpacket.service.impl;

import com.bonjour.practice.common.entity.RedPacket;
import com.bonjour.practice.common.mapper.RedPacketMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.module.redpacket.service.RedPacketService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @authur tc
 * @date 2023/2/7 10:18
 */
@Slf4j
@Service
public class RedPacketServiceImpl implements RedPacketService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CommonService commonService;

    @Autowired
    private RedPacketMapper redPacketMapper;

    public static final Map<String, Boolean> MAP = new HashMap<>();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendRedPacket(RedPacket redPacket) {
        String id = CommonUtils.getUUID();
        System.out.println(id);
        redPacket.setId(id);
        redPacket.setSendTime(new Date());
        redPacket.setLastMoney(redPacket.getTotalMoney());
        commonService.insert(redPacket, RedPacketMapper.class);
        try {
//            redisUtil.setCacheObjectAndExpire(redPacket.getId(), CommonUtils.beanToString(redPacket), 24 * 60 * 60, TimeUnit.SECONDS);
            redisUtil.setCacheObject(redPacket.getId(), CommonUtils.beanToString(redPacket));
        } catch (Exception e) {
            log.error("红包加入缓存失败！" + e.getMessage());
            throw new RuntimeException("发红包失败！");
        }
    }

    // 抢红包 二倍均值吗
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal getPacket(String userId, String packetId) {
        RLock lock = redissonClient.getLock("packet:id" + packetId);
        BigDecimal min = new BigDecimal("0.00");
        try {
            //                            等待时间  释放锁的时间    单位
            boolean isLock = lock.tryLock(20, 10, TimeUnit.SECONDS);
            if (isLock) {
                String s = (String) redisUtil.getCacheObject(packetId);
                if (StringUtils.isBlank(s)) {
                    throw new RuntimeException("红包去火星了。");
                }
                RedPacket redPacket = CommonUtils.stringToBean(s, RedPacket.class);
                if (redPacket == null) {
                    throw new RuntimeException("红包去火星了。");
                }
                if (System.currentTimeMillis() - redPacket.getSendTime().getTime() > 24 * 60 * 60 * 1000) {
                    throw new RuntimeException("红包已过期。");
                }
                if (redPacket.getNumber() <= 0) {
                    throw new RuntimeException("手慢了，红包抢完了！");
                }
                if (redPacket.getNumber() == 1) {
                    // 插入红包明细表
                    redPacket.setStatus("1");
                    redPacket.setNumber(0);
                    System.out.println("最后抢到了【" + redPacket.getLastMoney() + "】");
                    redPacket.setLastMoney(new BigDecimal("0.00"));
                    redisUtil.setCacheObject(packetId, CommonUtils.beanToString(redPacket));
                    return redPacket.getLastMoney();
                }
                min = splitPacket(redPacket.getNumber(), redPacket.getLastMoney());
                redPacket.setNumber(redPacket.getNumber() - 1);
                redPacket.setLastMoney(redPacket.getLastMoney().subtract(min));
                redisUtil.setCacheObject(packetId, CommonUtils.beanToString(redPacket));
                System.out.println("抢到了【" + min + "】");
                /**
                 *     BigDecimal remain = amount.subtract(min.multiply(num));
                 *     final Random random = new Random();
                 *     final BigDecimal hundred = new BigDecimal("100");
                 *     final BigDecimal two = new BigDecimal("2");
                 *     BigDecimal sum = BigDecimal.ZERO;
                 *     BigDecimal redpeck;
                 *     for (int i = 0; i < num.intValue(); i++) {
                 *         final int nextInt = random.nextInt(100);
                 *         if(i == num.intValue() -1){
                 *             redpeck = remain;
                 *         }else{
                 *             redpeck = new BigDecimal(nextInt).multiply(remain.multiply(two).divide(num.subtract(new BigDecimal(i)),2,RoundingMode.CEILING)).divide(hundred,2, RoundingMode.FLOOR);
                 *         }
                 *         if(remain.compareTo(redpeck) > 0){
                 *             remain = remain.subtract(redpeck);
                 *         }else{
                 *             remain = BigDecimal.ZERO;
                 *         }
                 *         sum = sum.add(min.add(redpeck));
                 *         System.out.println("第"+(i+1)+"个人抢到红包金额为："+min.add(redpeck));
                 *     }
                 */
            } else {
                log.error("{}加锁失败", packetId);
                throw new RuntimeException("系统繁忙！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("网络繁忙！");
        } finally {
            lock.unlock();
        }
        return min;
    }

    // 2倍均值法 2 * 剩余金额 / 剩余红包数
    public static BigDecimal splitPacket(Integer num, BigDecimal amount) {
        log.info("【" + num + "】，" );
        BigDecimal result = new BigDecimal("0.00");
        String n = String.valueOf(num - 1);
        BigDecimal min = new BigDecimal("0.01").multiply(new BigDecimal(n));
        BigDecimal last = amount.subtract(min);
        Random random = new Random();
        final int nextInt = random.nextInt(100);
        result = new BigDecimal(nextInt).multiply(last.multiply(new BigDecimal("2"))
                .divide(new BigDecimal(n), 2, RoundingMode.CEILING))
                .divide(new BigDecimal("100.00"), 2, RoundingMode.FLOOR);
        return result;
    }

    public static void main(String[] args) {
        int nest = new Random().nextInt(100);
        System.out.println(splitPacket(5, new BigDecimal("100.00")));
    }
}
