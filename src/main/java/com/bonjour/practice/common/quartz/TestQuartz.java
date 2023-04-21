package com.bonjour.practice.common.quartz;

import com.bonjour.practice.common.entity.JobTasks;
import com.bonjour.practice.common.mapper.JobTasksMapper;
import com.bonjour.practice.common.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.remote.rmi._RMIConnection_Stub;

/**
 * @authur tc
 * @date 2023/2/13 17:51
 */
@Slf4j
public class TestQuartz implements Job {

    @Autowired
    private CommonService commonService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(jobExecutionContext.getJobDetail() + "start--------------------------");
        System.out.println(jobExecutionContext.getJobDetail().getKey());
        JobTasks jobTasks = commonService.getMapper(JobTasksMapper.class).query().eq("job_name", jobExecutionContext.getJobDetail().getKey().getName()).one();
        if (jobTasks != null) {
            if ("0".equals(jobTasks.getStatus())) {
                jobTasks.setStatus("1");
                commonService.updateById(jobTasks, JobTasksMapper.class);
            }
        }
    }
}
