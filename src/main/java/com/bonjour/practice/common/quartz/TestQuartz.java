package com.bonjour.practice.common.quartz;

import com.bonjour.practice.common.entity.JobTasks;
import com.bonjour.practice.common.entity.User;
import com.bonjour.practice.common.mapper.JobTasksMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    public static void main(String[] args) {
        User user = new User();
        user.setPassword(null);
        System.out.println(StringUtils.isNotBlank(user.getPassword()));
    }
}
