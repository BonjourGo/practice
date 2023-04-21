package com.bonjour.practice.common.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bonjour.practice.common.entity.JobTasks;
import com.bonjour.practice.common.mapper.JobTasksMapper;
import com.bonjour.practice.common.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @authur tc
 * @date 2023/2/14 9:47
 */
@Slf4j
@Configuration
public class BasicQuartzScheduler {


    @Autowired
    private Scheduler scheduler;

    @Autowired
    private CommonService commonService;

    private final String name = "checkTask";

    private final String groupName = "checkTaskGroup";

    public void startJob() throws SchedulerException {
        log.info("任务开启");
        List<JobTasks> list = commonService.getMapper(JobTasksMapper.class).query().eq("status", "0").list();
        for (JobTasks jobTasks : list) {
            JobKey jobKey = new JobKey(jobTasks.getJobName(), jobTasks.getJobGroup());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail != null) {
                scheduler.start();
                jobTasks.setStatus("1");
                commonService.updateById(jobTasks, JobTasksMapper.class);
            }
        }

//        if (jobDetail == null) {
//            addJob(CheckTaskQuartz.class, name, groupName, "0 0/1 * * * ? ");
//        }
        // 其他任务


    }

    public void addOrUpdateJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobCron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger==null) {
                addJob(jobClass, jobName, jobGroupName, jobCron);
            } else {
                if (trigger.getCronExpression().equals(jobCron)) {
                    return;
                }
//                updateJob(jobName, jobGroupName, jobCron);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加job
     * @param jobClass
     * @param jobName
     * @param group
     * @param time
     * @throws SchedulerException
     */
    public void addJob(Class<? extends Job> jobClass, String jobName, String group, String time) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, group).build();
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, group)
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
        JobTasks jobTasks = new JobTasks();
        jobTasks.setJobGroup(group);
        jobTasks.setId(UUID.randomUUID().toString());
        jobTasks.setJobName(jobName);
        jobTasks.setStatus("0");
        jobTasks.setJobTime(time);
        jobTasks.setCreateTime(new Date());
        commonService.insert(jobTasks, JobTasksMapper.class);
    }

    /**
     * 暂停任务
     * @param jobName
     * @param jobGroupName
     */
    public void pauseJob(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException("暂停任务失败！" + e.toString());
        }
    }

    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
//        String formatTimeStr = null;
//        Date date = new Date();
//        Date date1 = new SimpleDateFormat("yyyyMMddHHmmss").parse("20230214154700");
//        System.out.println(date1);
//        if (Objects.nonNull(date)) {
//            formatTimeStr = sdf.format(date1);
//        }
//        System.out.println(formatTimeStr);
        String s = "[[\"640000000000\",\"640100000000\"],[\"640000000000\",\"640300000000\"]]";
        String str = "[[\"640000000000\"]]";
        JSONArray array = JSON.parseArray(str);
        for (Object o : array) {
            List<String> list = (List<String>) o;
            for (String s1 : list) {
                System.out.println(s1);
            }
        }
    }

}
