package com.bonjour.practice;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bonjour.practice.common.annotations.Limiter;
import com.bonjour.practice.common.annotations.RepeatLimit;
import com.bonjour.practice.common.annotations.RepeatLimitV2;
import com.bonjour.practice.common.entity.*;
import com.bonjour.practice.common.enums.OrderStatusEnum;
import com.bonjour.practice.common.mapper.*;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.service.IdService;
import com.bonjour.practice.common.utils.*;
import com.bonjour.practice.module.booking.service.BookingService;
import com.bonjour.practice.module.order.controller.OrderController;
import com.bonjour.practice.module.redpacket.controller.RedPacketController;
import com.bonjour.practice.rabbitmq.RabbitMQConfig;
import com.bonjour.practice.rabbitmq.RabbitMQProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    @Autowired
    private RedPacketController redPacketController;

    @Autowired
    private BookingService bookingService;

    private final Map<String, Boolean> map = new ConcurrentHashMap<String, Boolean>();

    private static final ExecutorService service = ThreadPoolUtils.getExector();

    public static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    @GetMapping("/testdate")
    @ApiOperation("/testdate")
    public String testdate(String key, String number, HttpServletResponse response) {
//        List<Product> products = productMapper.query().list();
//        ExcelUtil.exportExcel(products, "test", Product.class, response);
//        Long start = System.currentTimeMillis();
//        method01();
//        method02();
//        Map map = new HashMap();
//        map.put();
        threadLocal.set("hello");
        System.out.println("主线程" + Thread.currentThread().getName() + "获取 = " + threadLocal.get());
        CompletableFuture<String> future01 = CompletableFuture.supplyAsync(this::method01, service);
        CompletableFuture<String> future02 = CompletableFuture.supplyAsync(this::method02, service);
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(future01, future02);
        List<String> list = new ArrayList<>();
        CompletableFuture<List<String>> result = completableFuture.thenApply(v -> {
            try {
                list.add(future01.get());
                list.add(future02.get());
                return list;
            } catch (Exception ex) {
            }
            return null;
        });
        List<String> list1 = result.join();
        System.out.println(list1);
//        Long end = System.currentTimeMillis();
//        long time = (end - start) / 1000;
//        System.out.println(time + "s");
//        User user = new User();
//        String s = user.getNickName().substring(0, 10);
//        List<String> list = redisUtil.getIndex(key);
//        System.out.println(list);
//        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
//        copyOnWriteArrayList.add("s");
//        int index = list.indexOf(number);
//        redisUtil.removeKeyForList(key, number);
//        System.out.println(list.lastIndexOf(number));
//        System.out.println(index > 0 ? index : "没有预约");
//        LongAdder longAdder
        return "20240307";
    }

    @GetMapping("/repeat01")
    @ApiOperation("/repeat01")
    @RepeatLimit(timeOut = 5)
    public String repeat01() {
        try {
            log.info("start repeat01");
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        return Thread.currentThread().getName() + "20240307";
    }

    @GetMapping("/repeat02")
    @ApiOperation("/repeat02")
    @RepeatLimitV2(timeOut = 5)
    public String repeat02() {
        try {
            log.info("start repeat02");
            Thread.sleep(2000);
        } catch (Exception e) {

        }
        return Thread.currentThread().getName() + "20240307";
    }

    public String method01() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        return Thread.currentThread().getName() + threadLocal.get();
    }

    public String method02() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        return Thread.currentThread().getName() + threadLocal.get();
    }

    @PostMapping("/file")
    @ApiOperation("/file")
    public void file(@RequestBody MultipartFile multipartFile) {
        System.out.println(multipartFile.getName());
    }

    @Autowired
    private IdService idService;

    @Autowired
    private BookingMapper bookingMapper;

    @GetMapping("/test")
    @ApiOperation("/test")
//    @RepeatLimit(timeOut = 1)
    @Transactional(rollbackFor = Exception.class)
    public String test(String param) throws InterruptedException {
//        System.out.println(idService.get("abc"));
//        long start = System.currentTimeMillis();
//        Booking booking = bookingMapper.selectById(param);
//        bookingService.testListenableFuture(param).addCallback(
//                success -> {
//                    System.out.println("success");
//                    booking.setStatus("3");
//                    bookingMapper.updateById(booking);
//                },
//                error -> {
//                    System.out.println("failed");
//                });
//        booking.setStatus("2");
//        bookingMapper.updateById(booking);
        for (int i = 0; i < 1000; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    idService.get("abc");
                }
            });
        }
//        System.out.println(IdUtil.getSnowflake(1L, 2L).nextId());
//        Thread.sleep(2000);
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
//        for (int i = 0; i < 15; i++) {
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
////                    orderController.orderRedisson(UUID.randomUUID().toString(), 2L);
//                    redPacketController.getPacket(UUID.randomUUID().toString(), param);
////                    orderController.orderSpecial(UUID.randomUUID().toString(), 2L);
//                    // do something
//                }
//            });
//        }
//        Thread.sleep(1000 * 30);
//        for (int i = 0; i < threads.length; i++) {
//            executorService.execute(threads[i]);
//        }
        // redis 减库存
//        for (int i = 0; i < 100; i++) {
//            System.out.println("第" + i + "次");
//            service.execute(new Runnable() {
//                @Override
//                public void run() {
//                    Long result = redisUtil.descStock(Long.parseLong(param));
//                    if (result == 1L) {
//                        map.put(Thread.currentThread().getName(), true);
//                        System.out.println(Thread.currentThread().getName() + "成功");
//                    } else {
//                        System.out.println(Thread.currentThread().getName() + "失败");
//                    }
//                }
//            });
//        }
        return "success";
    }

    @Async()
    public ListenableFuture<Boolean> testListenableFuture(String id) {
        try {
            Thread.sleep(10000);
        } catch (Throwable e){
            return AsyncResult.forExecutionException(e);
        }
        return AsyncResult.forValue(true);
    }

//    public static void main(String[] args) {
////        for (int i = 0; i < 100; i++) {
////            String phone = "88888888" + i;
////            new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    String string = restTemplate.getForObject("http://localhost:8080/order/orderSpecial?phone=" + phone + "&productId=1", String.class);
////                    System.out.println(string);
////                }
////            }).start();
////        }
////        AtomicInteger atomicInteger = new AtomicInteger(0);
////        System.out.println(atomicInteger.incrementAndGet());
////        String string = restTemplate.getForObject("http://localhost:8080/order/orderSpecial?phone=123456&productId=1", String.class);
//        Set<Integer> set = new HashSet<>();
//        Integer a = 15;
//        Integer b = 129;
//        set.add(a);
//        set.add(b);
//        System.out.println(a.equals(b));
//        System.out.println(set);
//    }

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
//        AtomicInteger atomicInteger = new AtomicInteger(0);
//        atomicInteger.incrementAndGet();
//        String phone = "";
        int va = 0;
        for (int i = 0; i < 15; i++) {
            Map map = new ConcurrentHashMap();
            service.execute(() -> System.out.println(Thread.currentThread().getName() + "====="));
//            service.execute(new Runnable() {
//                @Override
//                public void run() {
//                    Thread thread = Thread.currentThread();
//                    Booking booking = new Booking();
//                    booking.setBookingDate("20230627");
//                    booking.setWdId("88888888");
//                    booking.setPhone(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase(Locale.ROOT));
////                    System.out.println(JSON.toJSONString(booking));
//                    String n = bookingService.booking(booking);
//                    System.out.println(n);
////                    testLock(Long.valueOf(id));
////                    testNoLock(Long.valueOf(id));
//                }
//            });
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

    @Autowired
    private RedPacketMapper redPacketMapper;
    @Autowired
    private ProductMapper productMapper;
    @PostMapping("/test01")
    @ApiOperation("/test01")
//    @Limiter
    public void test01(@RequestBody RedPacket redPacket) {
//        String id = CommonUtils.getUUID();
//        System.out.println(id);
//        redPacket.setId(id);
//        redPacket.setSendTime(new Date());
//        redPacket.setLastMoney(redPacket.getTotalMoney());
//        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(redPacket), Map.class);
//        redisUtil.setForHash(id, map);
//        RedPacket redPacket1 = new RedPacket();
//        redPacket1.setId(UUID.randomUUID().toString());
//        redPacketMapper.insertBatch(Collections.singletonList(redPacket1));

        Product product = new Product();
        product.setId(100L);
        productMapper.insertBatch(Collections.singletonList(product));
        System.out.println("=========================");
//        System.out.println(redisUtil.getKeyForHash(id));
//        System.out.println(redisUtil.getKeyForHashField(id, "desc"));
//        System.out.println("aaa");
//        Order order = new Order();
//        order.setOrderId(redisUtil.getIncrIdString("orderId"));
//        order.setCreateTime(CommonUtils.getTimeStringNormal("yyyyMMddhhmmssSSS"));
//        order.setOrderStatus(OrderStatusEnum.未支付.getKey());
////        order.setProductId(productId);
//        order.setNumber(1);
////        order.setUserId(UserUtil.getUser().getId());
//        commonService.insert(order, OrderMapper.class);
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

    public static Long getMillis(String format) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String prefix = simpleDateFormat.format(date).substring(0, 10);
        if (StringUtils.isBlank(format)) {
            return calendar.getTimeInMillis();
        } else {
            try {
                date = simpleDateFormat.parse(prefix + " " + format);
            } catch (Exception e) {
                log.error("时间转换异常");
            }
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        }
    }

//    public static void main(String[] args) {
//
//    }

    public static String getBase64() {
        Map<String, Object> map = new HashMap<>();
        map.put("", "");
        Set set = new HashSet();
        set.add("aaa");
        Set set1 = new TreeSet();
        for (Object o : set) {

        }
        String base64 = "";
        File f = new File("D:\\责令限期退还社会保险待遇通知书.docx");
        byte[] content = new byte[(int) f.length()];
        InputStream in = null;
        try {
            in = new FileInputStream(f);
            for (int off = 0, read;
                 (read = in.read(content, off, content.length - off)) > 0;
                 off += read);

            base64 = DatatypeConverter.printBase64Binary(content);
        } catch (IOException e) {
            // Some error occured
        } finally {
            if (in != null)
                try { in.close(); } catch (IOException e) {}
        }
        return base64;
    }




    public static volatile Integer a = 1;

    public static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        Booking booking = new Booking(new BigDecimal(123), new BigDecimal(124));
        Booking booking1 = new Booking(new BigDecimal(124), new BigDecimal(125));
        Booking booking2 = new Booking(new BigDecimal(125), new BigDecimal(126));
        Booking booking3 = new Booking(new BigDecimal(127), new BigDecimal(128));
        List<Booking> books = new ArrayList<>();
        books.add(booking);
        books.add(booking1);
        books.add(booking2);
        books.add(booking3);
        Iterator<Booking> iterator = books.iterator();
        BigDecimal bigDecimal = BigDecimal.ZERO;
        BigDecimal next = BigDecimal.ZERO;
        BigDecimal curr = new BigDecimal(123);
        for (Booking book : books) {
            BigDecimal temp = book.getCurr();
            if (curr.compareTo(temp) == 0) {
                next = book.getNext();
                curr = next;
            } else {
                break;
            }
        }
//        while (iterator.hasNext()) {
//            BigDecimal temp = iterator.next().getCurr();
//           if (curr.compareTo(temp) == 0) {
//               next = iterator.next().getNext();
//               curr = next;
//           } else {
//               break;
//           }
//         }
        System.out.println(next);
//        readWriteLock.readLock().lock();
//        for (int i = 0; i < 10000; i++) {
//           service.submit(new Runnable() {
//               @Override
//               public void run() {
//                   a++;
//               }
//           });
//        }
//        try {
//            Thread.sleep(2000);
//        } catch (Exception e) {
//
//        }
//        System.out.println(a);
//        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(simpleDateFormat.format(date));
//        int e = 1;
//        for (int i = 0; i < 10; i++) {
//        }
//        System.out.println(e);
//        int a = 1;
//        int b = 0;
//        b = ++a;
//        System.out.println("a=" + a);
//        System.out.println("b=" + b);
//        int c = 1;
//        int d = 0;
//        d = c++;
//        System.out.println("c=" + c);
//        System.out.println("d=" + d);
//        for (int binCount = 0; ; ++binCount) {
//            System.out.println(binCount);
//            if (binCount >= 7) {
//                break;
//            }
//        }
    }
    private static ReentrantLock lock = new ReentrantLock();
    private static Integer number = 0;
    public void add() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            number++;
        } catch (Exception e) {

        } finally {
            reentrantLock.unlock();
        }
        System.out.println(number);
    }

}
