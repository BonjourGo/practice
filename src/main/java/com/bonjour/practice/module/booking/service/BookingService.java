package com.bonjour.practice.module.booking.service;

import com.bonjour.practice.common.entity.Booking;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @authur tc
 * @date 2023/6/26 16:31
 */
public interface BookingService {

    String booking(Booking booking);

    String next(String number, String type, String wdId);

    ListenableFuture<Boolean> testListenableFuture(String id);
}
