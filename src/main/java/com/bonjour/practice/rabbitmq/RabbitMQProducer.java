package com.bonjour.practice.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// 通用生产者发送消息
@Slf4j
@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDelayMsg(String exchangeName, String routingKey, Object msg, Integer delayTime) {
        log.info("发送消息 + " + msg);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, a -> {
            a.getMessageProperties().setDelay(delayTime);
            return a;
        });
    }
}
