package com.bonjour.practice;

import com.bonjour.practice.common.annotations.Limiter;
import com.bonjour.practice.common.annotations.RepeatLimit;
import com.bonjour.practice.common.entity.Order;
import com.bonjour.practice.common.entity.Product;
import com.bonjour.practice.common.entity.User;
import com.bonjour.practice.common.enums.OrderStatusEnum;
import com.bonjour.practice.common.mapper.OrderMapper;
import com.bonjour.practice.common.mapper.ProductMapper;
import com.bonjour.practice.common.mapper.UserMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.common.utils.ThreadPoolUtils;
import com.bonjour.practice.common.utils.UserUtil;
import com.bonjour.practice.module.order.controller.OrderController;
import com.bonjour.practice.rabbitmq.RabbitMQConfig;
import com.bonjour.practice.rabbitmq.RabbitMQProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/hello")
@Api("test API")
@Slf4j
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

    @Autowired
    private RedissonClient redissonClient;

    private final Map<String, Boolean> map = new ConcurrentHashMap<String, Boolean>();

    private final ExecutorService service = ThreadPoolUtils.getExector();

    @GetMapping("/test")
    @ApiOperation("/test")
    @RepeatLimit(timeOut = 1)
    public String test(String param) {
//        redisTemplate.opsForValue().set("aaa", "123");
//        System.out.println(redisTemplate.opsForValue().get("aaa"));
//        System.out.println(redisUtil.getIncrLongId("aaa"));
//        System.out.println(redisUtil.getIncrId("bbb"));
//        System.out.println(CommonUtils.getRequestIP(request));
//        User user = userMapper.selectById("0001");
//        Order order = orderMapper.selectById(1);
//        return CommonUtils.beanToString(user);
//        for (int i = 0; i < 100; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    orderController.test();
//                }
//            }).start();
//        }
//        for (int i = 0; i < 100; i++) {
//            String phone = "88888888" + i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String string = restTemplate.getForObject("http://localhost:8080/order/orderSpecial?phone=" + phone + "&productId=1", String.class);
//                    System.out.println(string);
//                }
//            }).start();
//        }
//        ExecutorService executorService = ThreadPoolUtils.getExector();
//        Thread []threads = new Thread[100];
//        for (int i = 0; i < 100; i++) {
////            threads[i] = new Thread(() -> {
////                // do something
////            });
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    // do something
//                }
//            });
//        }
//        for (int i = 0; i < threads.length; i++) {
//            executorService.execute(threads[i]);
//        }
        // redis 减库存
        for (int i = 0; i < 100; i++) {
            System.out.println("第" + i + "次");
            service.execute(new Runnable() {
                @Override
                public void run() {
                    Long result = redisUtil.descStock(Long.parseLong(param));
                    if (result == 1L) {
                        map.put(Thread.currentThread().getName(), true);
                        System.out.println(Thread.currentThread().getName() + "成功");
                    } else {
                        System.out.println(Thread.currentThread().getName() + "失败");
                    }
                }
            });
        }
        return "";
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 100; i++) {
//            String phone = "88888888" + i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String string = restTemplate.getForObject("http://localhost:8080/order/orderSpecial?phone=" + phone + "&productId=1", String.class);
//                    System.out.println(string);
//                }
//            }).start();
//        }
//        AtomicInteger atomicInteger = new AtomicInteger(0);
//        System.out.println(atomicInteger.incrementAndGet());
//        String string = restTemplate.getForObject("http://localhost:8080/order/orderSpecial?phone=123456&productId=1", String.class);
    }

    @GetMapping("/test02")
    @ApiOperation("test02")
    public void test02(String id) throws InterruptedException {
//        for (int i = 0; i < 100; i++) {
//            service.execute(new Runnable() {
//                @Override
//                public void run() {
//                    pro(id);
//                }
//            });
//        }
        AtomicInteger atomicInteger = new AtomicInteger(0);
        atomicInteger.incrementAndGet();
        String phone = "";
        int va = 0;
        for (int i = 0; i < 15; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
//                    testLock(Long.valueOf(id));
//                    testNoLock(Long.valueOf(id));
                }
            });
        }
    }

    public void pro(String id) {
        RLock lock = redissonClient.getLock(id + "lock");

        try {
//            System.out.println("start---------------------------------");
            boolean islock = lock.tryLock(10, 20, TimeUnit.SECONDS);
//            System.out.println("end---------------------------------");
            if (islock) {
                System.out.println(redisUtil.getCacheObject(id));
                String value = (String) redisUtil.getCacheObject(id);
                if (StringUtils.isBlank(value)) {
                    log.info("value is null");
                    return;
                }
                Integer integer = Integer.parseInt(value);
                if (integer > 0) {
                    redisUtil.decrId(id);
                    System.out.println(redisUtil.getCacheObject(id));
                } else {
                    System.out.println("stock is null");
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public void testLock(Long id) {
        Long result = redisUtil.descStock(id);
        if (result == 1L) {
            log.info("---------success");
            // success
            Product product = commonService.getMapper(ProductMapper.class).selectById(id);
            if (product == null) {
                throw new RuntimeException("当前商品已下架！换个其他商品吧！");
            }
            if (product.getStock() < 1) {
                throw new RuntimeException("当前商品已抢购完，试试其他商品吧！");
            }
//            product.setStock(product.getStock() - 1);
//            commonService.updateById(product, ProductMapper.class);
            commonService.getMapper(ProductMapper.class).updateStocks(id);
        } else if (result == 2L) {
            throw new RuntimeException("当前商品已下架，换个商品吧");
        }
    }

    public void testNoLock(Long id) {
//        if (map.containsKey(phone)) {
//            log.error("already buy!");
//        }
        Product product = commonService.getMapper(ProductMapper.class).selectById(id);
        if (product == null) {
            throw new RuntimeException("当前商品已下架！换个其他商品吧！");
        }
        if (product.getStock() < 1) {
            throw new RuntimeException("当前商品已抢购完，试试其他商品吧！");
        }
        log.info("抢到了！");
//        product.setStock(product.getStock() - 1);
        commonService.updateById(product, ProductMapper.class);
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

    @ApiOperation("hello")
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
