package com.bonjour.practice.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @authur tc
 * @date 2023/1/12 10:59
 */
@Slf4j
//@Component
public class RabbitMQCallback implements RabbitTemplate.ConfirmCallback {


    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if (b) {
            log.info("发送成功");
        } else {
            log.error("发送失败");
        }
    }
}
