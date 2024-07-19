package com.bonjour.practice.module.booking.controller;

import com.bonjour.practice.common.entity.Booking;
import com.bonjour.practice.common.utils.Result;
import com.bonjour.practice.module.booking.service.BookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @authur tc
 * @date 2023/6/26 16:30
 */
@Api(tags = "booking")
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/booking")
    @ApiOperation("/booking")
    public Result booking(@RequestBody Booking booking) {
        String number = bookingService.booking(booking);
        return Result.ok(number);
    }

    @GetMapping("/next")
    @ApiOperation("/next")
    public Result next(String number, String type, String wdId) {
        String next = bookingService.next(number, type, wdId);
        return Result.ok(next);
    }
}
