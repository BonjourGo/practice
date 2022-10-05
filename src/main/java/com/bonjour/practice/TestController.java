package com.bonjour.practice;

import com.bonjour.practice.common.entity.User;
import com.bonjour.practice.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/hello")
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/hello")
    public String hello() {
        User user = new User();
        user.setId("cxx");
        user.setName("cxx");
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NORMAL_NAME, "normal.test", "user");
        return "hello";
    }

    @GetMapping("/delay")
    public String delay() throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            Thread.sleep(1000);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
            String date = format.format(new Date());
            String msg = "第" + i + "次发送   " + date;
            System.out.println(msg);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAY_EXCHANG_NAME, "delay", msg, a -> {
                a.getMessageProperties().setDelay(10000);
                return a;
            });
//            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAY_EXCHANG_NAME, "delay", msg, new MessagePostProcessor() {
//                @Override
//                public Message postProcessMessage(Message message) throws AmqpException {
//                    message.getMessageProperties().setHeader("x-delay", 10000);
//                    return message;
//                }
//            });
        }
        return "send success";
    }
}
