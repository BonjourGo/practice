package com.bonjour.practice.module.order.service.impl;

import com.bonjour.practice.common.entity.Order;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.module.order.service.OrderService;
import com.bonjour.practice.rabbitmq.RabbitMQConfig;
import com.bonjour.practice.rabbitmq.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void order(Order order) {
        rabbitMQProducer.sendDelayMsg(RabbitMQConfig.DELAY_EXCHANG_NAME, RabbitMQConfig.DELAY_ROUTTINGKEY, CommonUtils.beanToString(order), 1000 * 20);
    }
}
