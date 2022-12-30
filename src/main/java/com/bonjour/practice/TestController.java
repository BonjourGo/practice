package com.bonjour.practice;

import com.bonjour.practice.common.annotations.Limiter;
import com.bonjour.practice.common.annotations.RepeatLimit;
import com.bonjour.practice.common.entity.Order;
import com.bonjour.practice.common.entity.User;
import com.bonjour.practice.common.enums.OrderStatusEnum;
import com.bonjour.practice.common.mapper.OrderMapper;
import com.bonjour.practice.common.mapper.UserMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.common.utils.UserUtil;
import com.bonjour.practice.module.order.controller.OrderController;
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
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private OrderController orderController;

    @Autowired
    private CommonService commonService;

    @Autowired
    private static RestTemplate restTemplate;

    @GetMapping("/test")
    @ApiOperation("/test")
    @RepeatLimit(timeOut = 1)
    public String test(HttpServletRequest request) {
//        redisTemplate.opsForValue().set("aaa", "123");
//        System.out.println(redisTemplate.opsForValue().get("aaa"));
//        System.out.println(redisUtil.getIncrLongId("aaa"));
//        System.out.println(redisUtil.getIncrId("bbb"));
//        System.out.println(CommonUtils.getRequestIP(request));
//        User user = userMapper.selectById("0001");
//        Order order = orderMapper.selectById(1);
//        return CommonUtils.beanToString(user);
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    orderController.test();
                }
            }).start();
        }
        return "";
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            String phone = "88888888" + i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String string = restTemplate.getForObject("http://localhost:8080/order/orderSpecial?phone=" + phone + "&productId=1", String.class);
                    System.out.println(string);
                }
            }).start();
        }
//        String string = restTemplate.getForObject("http://localhost:8080/order/orderSpecial?phone=123456&productId=1", String.class);
    }

    @GetMapping("/test01")
    @ApiOperation("/test01")
    @Limiter
    public void test01() {
        System.out.println("aaa");
        Order order = new Order();
        order.setOrderId(redisUtil.getIncrIdString("orderId"));
        order.setCreateTime(CommonUtils.getTimeStringNormal("yyyyMMddhhmmssSSS"));
        order.setOrderStatus(OrderStatusEnum.未支付.getKey());
//        order.setProductId(productId);
        order.setNumber(1);
//        order.setUserId(UserUtil.getUser().getId());
        commonService.insert(order, OrderMapper.class);
//        for (int i = 0; i < 100; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    orderController.test01();
//                }
//            }).start();
//        }
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
