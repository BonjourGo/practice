package com.bonjour.practice.module.redpacket.service.impl;

import com.bonjour.practice.common.entity.Product;
import com.bonjour.practice.common.entity.RedPacket;
import com.bonjour.practice.common.mapper.RedPacketMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.module.redpacket.service.RedPacketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

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
//        commonService.insert(redPacket, RedPacketMapper.class);
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
                if (redPacket.getIds().contains(userId)) {
                    log.info("抢过了！");
                    return new BigDecimal("0.00");
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
                .divide(new BigDecimal(num), 2, RoundingMode.CEILING))
                .divide(new BigDecimal("100.00"), 2, RoundingMode.FLOOR);
        return result;
    }

    public static List<BigDecimal> splitPackets(Integer num, BigDecimal amount) {
        List<BigDecimal> list = new ArrayList<>();
        log.info("【" + num + "】，" );
        if (num == 1) {
            list.add(amount);
            return list;
        }
        for (Integer integer = 0; integer < num; integer++) {
            Integer count = num - integer;
            if (count == 1) {
                System.out.println("第" + (integer + 1)  + "次【" + amount + "】" + "剩余【" + "0" + "】");
                list.add(amount);
                break;
            }
            BigDecimal result = new BigDecimal("0.00");
            String n = String.valueOf(count - 1);
            BigDecimal min = new BigDecimal("0.01").multiply(new BigDecimal(n));
            BigDecimal last = amount.subtract(min);
            Random random = new Random();
            final int nextInt = random.nextInt(100);
            result = new BigDecimal(nextInt).multiply(
                    last.multiply(new BigDecimal("2")).divide(new BigDecimal(count), 2, RoundingMode.CEILING)
                    )
                    .divide(new BigDecimal("100.00"), 2, RoundingMode.FLOOR);
            amount = amount.subtract(result);
            list.add(result);
            System.out.println("第" + (integer + 1)  + "次【" + result + "】" + "剩余【" +amount + "】");
        }
        return list;
    }

    public static volatile long a = 0;
    public static AtomicLong atomicLong = new AtomicLong(0);
    public static void main(String[] args) {
//        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
//        concurrentHashMap.size();
//        HashMap hashMap = new HashMap();
//        hashMap.size();
////        int nest = new Random().nextInt(100);
////        System.out.println(splitPackets(5, new BigDecimal("100.00")));
//        for (int i = 0; i < 5000; i++) {
//            new Thread(() -> {
//                atomicLong.incrementAndGet();
//            }).start();
//        }
//        try {
//            Thread.sleep(2000);
//        } catch (Exception e) {
//
//        }
//        System.out.println(atomicLong);
        System.out.println(10 >> 1);
        LinkedList list = new LinkedList();
        List<String> list1 = new ArrayList<>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
//        for (String s : list1) {
//            list1.remove(s);
//        }
        Iterator<String> iterator = list1.iterator();
//        Iterable
        while (iterator.hasNext()) {
            if ("c".equals(iterator.next())) {
                iterator.remove();
                break;
            }
        }
        System.out.println(list1);
        Product product = new Product();
        product.setId(1L);
        Product product1 = new Product();
        product1.setId(2L);
        System.out.println(product.equals(product1));
    }
}
