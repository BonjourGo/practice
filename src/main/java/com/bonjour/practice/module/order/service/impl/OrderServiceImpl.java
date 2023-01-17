package com.bonjour.practice.module.order.service.impl;

import com.bonjour.practice.common.entity.Order;
import com.bonjour.practice.common.entity.Product;
import com.bonjour.practice.common.enums.OrderStatusEnum;
import com.bonjour.practice.common.mapper.OrderMapper;
import com.bonjour.practice.common.mapper.ProductMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.common.utils.UserUtil;
import com.bonjour.practice.module.order.service.OrderService;
import com.bonjour.practice.rabbitmq.RabbitMQConfig;
import com.bonjour.practice.rabbitmq.RabbitMQProducer;
import com.sun.org.apache.xpath.internal.operations.Bool;
import jdk.internal.dynalink.linker.LinkerServices;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CommonService commonService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    private static Map<String, Boolean> map = new HashMap<>();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void order(Order order) {
        // 判断库存
        if (commonService.getMapper(ProductMapper.class).query().ge("stock", order.getNumber()).count() < 0) {
            throw new RuntimeException("库存不足！");
        }
        Product product = commonService.getMapper(ProductMapper.class).selectById(order.getProductId());
        product.setStock(product.getStock() - order.getNumber());
        productMapper.updateById(product);
//        order.setUserId(UserUtil.getUser().getId());
        order.setOrderId(redisUtil.getIncrId("orderId"));
        order.setCreateTime(CommonUtils.getTimeStringNormal("yyyyMMddhhmmssSSS"));
        order.setOrderStatus(OrderStatusEnum.未支付.getKey());
        commonService.insert(order, OrderMapper.class);
//        rabbitMQProducer.sendDelayMsg(RabbitMQConfig.DELAY_EXCHANG_NAME, RabbitMQConfig.DELAY_ROUTTINGKEY, CommonUtils.beanToString(order), 1000 * 20);
    }

    // redis lua 脚本减库存
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderSpecial(String phone, Long productId) {
        if (map.containsKey(phone) && map.get(phone)) {
            throw new RuntimeException("do not repeat order!");
        }
        // redis 减库存
        Long result = redisUtil.descStock(productId);
        if (result == 1L) {
            // success
            Product product = commonService.getMapper(ProductMapper.class).selectById(productId);
            if (product == null) {
                redisUtil.deleteObject("id_" + productId);
                throw new RuntimeException("当前商品已下架！换个其他商品吧！");
            }
            if (product.getStock() < 1) {
                redisUtil.deleteObject("id_" + product.getId());
                throw new RuntimeException("当前商品已抢购完，试试其他商品吧！");
            }
            Order order = new Order();
            order.setOrderId(redisUtil.getIncrIdString("orderId"));
            order.setCreateTime(CommonUtils.getTimeStringNormal("yyyyMMddhhmmssSSS"));
            order.setOrderStatus(OrderStatusEnum.未支付.getKey());
            order.setProductId(productId);
            order.setNumber(1);
            order.setUserId(UserUtil.getUser().getId());
            commonService.insert(order, OrderMapper.class);
            product.setStock(product.getStock() - 1);
            commonService.updateAllById(product, ProductMapper.class);
            map.put(phone, true);
            rabbitMQProducer.sendDelayMsg(RabbitMQConfig.DELAY_EXCHANG_NAME, RabbitMQConfig.DELAY_ROUTTINGKEY, CommonUtils.beanToString(order), 30 * 60);
        } else if (result == 2L) {
            throw new RuntimeException("当前商品已下架，换个商品吧");
        }
    }

    // redisson 分布式锁
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderForRedisson(String phone, Long productId) {
        if (map.containsKey(phone) && map.get(phone)) {
            throw new RuntimeException("do not repeat order!");
        }
        RLock lock = redissonClient.getLock(phone + productId + "lock");
        try {
            boolean isLock = lock.tryLock(20, 10, TimeUnit.SECONDS);
            if (isLock) {
                log.info("加锁成功！");
                Product product = commonService.getMapper(ProductMapper.class).selectById(productId);
                if (product == null || product.getStock() < 1 ) {
                    throw new RuntimeException("当前商品已下架！换个其他商品吧！");
                }
                Order order = new Order();
                order.setOrderId(redisUtil.getIncrIdString("orderId"));
                order.setCreateTime(CommonUtils.getTimeStringNormal("yyyyMMddhhmmssSSS"));
                order.setOrderStatus(OrderStatusEnum.未支付.getKey());
                order.setProductId(productId);
                order.setNumber(1);
                order.setUserId(UserUtil.getUser().getId());
                commonService.insert(order, OrderMapper.class);
                product.setStock(product.getStock() - 1);
                commonService.updateAllById(product, ProductMapper.class);
                map.put(phone, true);
            } else {
                log.error("抢购加锁失败！");
                throw new RuntimeException("系统繁忙请稍后再试！");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void init() {
        List<Product> products = commonService.getMapper(ProductMapper.class).query().eq("product_status", "2").list();
        for (Product product : products) {
            if (product.getStock() > 0) {
                String key = "id_" + product.getId();
                try {
                    redisUtil.setCacheObject(key, product.getStock());
                } catch (Exception e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }
    }
}
