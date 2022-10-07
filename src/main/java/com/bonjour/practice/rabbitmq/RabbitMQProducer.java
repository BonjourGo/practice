package com.bonjour.practice.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// 通用生产者发送消息
@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDelayMsg(String exchangeName, String routingKey, Object msg, Integer delayTime) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, a -> {
            a.getMessageProperties().setDelay(delayTime);
            return a;
        });
    }
}
