package com.bonjour.practice.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

// 通用生产者发送消息
@Slf4j
@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitMQCallback rabbitMQCallback;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initCallback() {
        rabbitTemplate.setConfirmCallback(rabbitMQCallback);
    }

//    public void send

    public void sendDelayMsg(String exchangeName, String routingKey, Object msg, Integer delayTime) {
        log.info("发送消息 + " + msg);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, a -> {
            a.getMessageProperties().setDelay(delayTime);
            return a;
        });
    }
}
