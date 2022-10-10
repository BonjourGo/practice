package com.bonjour.practice.module.order.service.impl;

import com.bonjour.practice.common.entity.Order;
import com.bonjour.practice.common.entity.Product;
import com.bonjour.practice.common.enums.OrderStatusEnum;
import com.bonjour.practice.common.mapper.OrderMapper;
import com.bonjour.practice.common.mapper.ProductMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.module.order.service.OrderService;
import com.bonjour.practice.rabbitmq.RabbitMQConfig;
import com.bonjour.practice.rabbitmq.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CommonService commonService;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void order(Order order) {
        // 判断库存
        if (commonService.getMapper(ProductMapper.class).query().ge("stock", order.getNumber()).count() < 0) {
            throw new RuntimeException("库存不足！");
        }
        Product product = commonService.getMapper(ProductMapper.class).selectById(order.getProductId());
        product.setStock(product.getStock() - order.getNumber());
        commonService.updateAllById(product, ProductMapper.class);
        order.setOrderId(RedisUtil.getIncrId("orderId"));
        order.setCreateTime(CommonUtils.getTimeStringNormal("yyyyMMddhhmmssSSS"));
        order.setOrderStatus(OrderStatusEnum.未支付.getKey());
        commonService.insert(order, OrderMapper.class);
        rabbitMQProducer.sendDelayMsg(RabbitMQConfig.DELAY_EXCHANG_NAME, RabbitMQConfig.DELAY_ROUTTINGKEY, CommonUtils.beanToString(order), 1000 * 20);
    }
}
