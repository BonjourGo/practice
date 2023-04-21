package com.bonjour.practice.common.quartz;

import com.bonjour.practice.common.entity.JobTasks;
import com.bonjour.practice.common.mapper.JobTasksMapper;
import com.bonjour.practice.common.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

/**
 * @authur tc
 * @date 2023/2/14 10:00
 */
@Slf4j
@Configuration
public class CheckTaskListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CommonService commonService;

    private BasicQuartzScheduler basicQuartzScheduler;

    @Autowired
    public CheckTaskListener(BasicQuartzScheduler basicQuartzScheduler) {
        this.basicQuartzScheduler = basicQuartzScheduler;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            log.info("定时任务已开启-------");
            basicQuartzScheduler.startJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
