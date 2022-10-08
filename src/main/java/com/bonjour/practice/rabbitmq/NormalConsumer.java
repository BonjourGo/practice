package com.bonjour.practice.rabbitmq;

import com.bonjour.practice.common.entity.User;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * normal 消费者
 */
@Slf4j
@Component
public class NormalConsumer {

    // 监听普通队列
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NORMAL_NAME)
    public void normalConsumer(Channel channel, Message message) throws IOException {
        // 业务处理
        log.info(message.toString());
        try {
            log.info("收到消息" + new String(message.getBody()));
            Thread.sleep(2000);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("消息处理成功！");
        } catch (Exception e) {
            // 失败
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            log.info("消息处理失败！");
        }
    }

    // 监听延迟队列
//    @RabbitListener(queues = RabbitMQConfig.DELAY_QUEUE_NAME)
//    public void delayConsumer(Channel channel, Message message) throws IOException {
//        // 业务处理
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
//            String date = format.format(new Date());
//            log.info(date + "收到消息" + new String(message.getBody()));
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            log.info("消息处理成功！");
//        } catch (Exception e) {
//            // 失败
//            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
//            log.info("消息处理失败！");
//        }
//    }
}
