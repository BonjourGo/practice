package com.bonjour.practice.module.booking.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bonjour.practice.common.entity.Booking;
import com.bonjour.practice.common.entity.User;
import com.bonjour.practice.common.mapper.BookingMapper;
import com.bonjour.practice.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @authur tc
 * @date 2023/6/26 16:31
 */
@Slf4j
@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private RedissonClient redissonClient;

    public BookingServiceImpl() {

    }

    public BookingServiceImpl(RedisUtil redisUtil, BookingMapper bookingMapper, RedissonClient redissonClient) {
        this.bookingMapper = bookingMapper;
        this.redisUtil = redisUtil;
        this.redissonClient = redissonClient;
    }

    public static List<String> getAllMonths(String months) {
        List<String> list = new ArrayList<>();
        String[] strings = months.split(",");
        int startYear = parse(strings[0], "1");
        int startMonth = parse(strings[0], "2");
        YearMonth startYearMonth = YearMonth.of(startYear, startMonth);
        YearMonth endYearMonth = YearMonth.of(parse(strings[0], "1"), parse(strings[0], "2"));
        System.out.println(startYearMonth);
        System.out.println(endYearMonth);
        YearMonth currentYearMonth = startYearMonth;
        while (!currentYearMonth.isAfter(endYearMonth)) {
            System.out.println(currentYearMonth);
            currentYearMonth = currentYearMonth.plusMonths(1);
        }
        return list;
    }

    public static int parse(String string, String type) {
        if ("1".equals(type)) {
            return Integer.parseInt(string.substring(0, 4));
        } else {
            return Integer.parseInt(string.substring(4, 6));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        User user = new User();
        if ("1".equals(user.getPhone())) {
            System.out.println("6576879876");
        }
        List<String> months = getAllMonths("202210,202310");
        System.out.println(months);
        Long start = System.currentTimeMillis();
        Thread.sleep(5000);
        Long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        int integer = list.indexOf("5");
        System.out.println(integer > -1 ? integer + 1 : "没有排队信息");
    }

    @Override
    public String booking(Booking booking) {
        System.out.println(JSON.toJSONString(booking));
        System.out.println(StringUtils.isBlank(booking.getWdId()));
        String key = "id_" + booking.getWdId() + booking.getBookingDate();
        RLock lock = redissonClient.getLock(key);
        String number = "";
        try {
            Boolean isLock = lock.tryLock(20, 20, TimeUnit.SECONDS);
            if (isLock) {
                if (bookingMapper.query().eq("wd_id", booking.getWdId())
                        .eq("booking_date", booking.getBookingDate()).eq("status", "1").count() >= 10) {
                    throw new RuntimeException("预约人数已满！");
                }
                if (bookingMapper.query().eq("wd_id", booking.getWdId())
                        .eq("booking_date", booking.getBookingDate()).eq("status", "1").eq("phone", booking.getPhone()).count() > 0) {
                    throw new RuntimeException("不能重复预约！");
                }
                number = redisUtil.getIncrIdString("id_" + key);
                booking.setId(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase(Locale.ROOT));
                booking.setStatus("1");
                booking.setNumber(number);
                bookingMapper.insert(booking);
                redisUtil.setKeyLeftForList(key, number);
            } else {
                log.error("加锁失败！");
                throw new RuntimeException("预约失败!请稍后再试");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        } finally {
            lock.unlock();
        }
        return number;
    }

    @Override
    public String next(String number, String type, String wdId) {
        LambdaQueryWrapper<Booking> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Booking::getNumber, number);
        Booking booking = bookingMapper.selectOne(lambdaQueryWrapper);
        if (booking != null) {
            booking.setStatus(type);
            bookingMapper.updateById(booking);
        }
        return (String) redisUtil.getKeyRightForList("id_" + wdId + parseDate());
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ListenableFuture<Boolean> testListenableFuture(String id) {
        try {
            Thread.sleep(10000);
            Booking booking = new Booking();
            booking.setId(cn.hutool.core.lang.UUID.fastUUID().toString());
            booking.setPhone("123456");
            bookingMapper.insert(booking);
        } catch (Throwable e){
            return AsyncResult.forExecutionException(e);
        }
        return AsyncResult.forValue(true);
    }

    public String parseDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }
}
