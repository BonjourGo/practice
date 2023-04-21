package com.bonjour.practice.common.quartz;

import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @authur tc
 * @date 2023/2/14 10:19
 */
@RestController
@RequestMapping("/quartz")
public class QuartzController {

    @Autowired
    private BasicQuartzScheduler basicQuartzScheduler;

    @ApiOperation("/add")
    @GetMapping("/add")
    public void add(String time, String name) throws SchedulerException {
        basicQuartzScheduler.addJob(TestQuartz.class, name, "test", time);
    }
}
