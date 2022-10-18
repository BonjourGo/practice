package com.bonjour.practice;

import com.bonjour.practice.common.annotations.RepeatLimit;
import com.bonjour.practice.common.entity.Order;
import com.bonjour.practice.common.entity.User;
import com.bonjour.practice.common.mapper.OrderMapper;
import com.bonjour.practice.common.mapper.UserMapper;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.rabbitmq.RabbitMQConfig;
import com.bonjour.practice.rabbitmq.RabbitMQProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/hello")
@Api("test API")
public class TestController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @GetMapping("/test")
    @ApiOperation("/test")
    @RepeatLimit(timeOut = 1)
    public String test(HttpServletRequest request) {
        redisTemplate.opsForValue().set("aaa", "123");
        System.out.println(redisTemplate.opsForValue().get("aaa"));
        System.out.println(redisUtil.getIncrLongId("aaa"));
        System.out.println(redisUtil.getIncrId("bbb"));
        System.out.println(CommonUtils.getRequestIP(request));
        User user = userMapper.selectById("0001");
        Order order = orderMapper.selectById(1);
        return CommonUtils.beanToString(user);
    }

    @GetMapping("/hello")
    public String hello() {
        User user = new User();
        user.setId("cxx");
        user.setNickName("cxx");
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
            rabbitMQProducer.sendDelayMsg(RabbitMQConfig.DELAY_EXCHANG_NAME, "delay", msg, 10000);
//            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAY_EXCHANG_NAME, "delay", msg, a -> {
//                a.getMessageProperties().setDelay(10000);
//                return a;
//            });
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
