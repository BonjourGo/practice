package com.bonjour.practice.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * rabbitMQ 配置类
 */
//@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NORMAL_NAME = "normal_exchange";

    public static final String QUEUE_NORMAL_NAME = "normal_queue";

    public static final String NORMAL_ROUTINGKEY = "normal.#";

    public static final String DELAY_EXCHANG_NAME = "delay_exchange";

    public static final String DELAY_QUEUE_NAME = "delay_queue";

    public static final String DELAY_ROUTTINGKEY = "delay";

    // 创建交换机
    @Bean("normalExchange")
    public Exchange normalExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NORMAL_NAME).durable(true).build();
    }

    // 创建队列
    @Bean("normalQueue")
    public Queue normalQueue() {
        return QueueBuilder.durable(QUEUE_NORMAL_NAME).build();
    }

    // 绑定交换机和队列
    @Bean
    public Binding bindingNormal(@Qualifier("normalExchange") Exchange exchange, @Qualifier("normalQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(NORMAL_ROUTINGKEY).noargs();
    }

    // 延迟队列交换机
//    @Bean("delayExchange")
//    public CustomExchange delayExchange() {
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        // 交换机名称 交换机类型 是否持久化 是否自动删除 参数
//        return new CustomExchange(DELAY_EXCHANG_NAME, "x-delayed-message", true, false, args);
//    }
//
//    // 延迟队列
//    @Bean("delayQueue")
//    public Queue delayQueue() {
//        return QueueBuilder.durable(DELAY_QUEUE_NAME).build();
//    }
//
//    // 绑定延迟队列和交换机
//    @Bean
//    public Binding bindingDelay(@Qualifier("delayExchange") Exchange exchange, @Qualifier("delayQueue") Queue queue) {
//        return BindingBuilder.bind(queue).to(exchange).with(DELAY_ROUTTINGKEY).noargs();
//    }

    public static String parseDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String day = "";
        try {
            day = simpleDateFormat.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return day;
    }

    public static void main(String[] args) {
        System.out.println(parseDate(new Date()));
    }
}
