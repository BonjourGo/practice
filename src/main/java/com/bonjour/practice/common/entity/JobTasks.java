package com.bonjour.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @authur tc
 * @date 2023/2/14 9:48
 */
@Data
@ApiModel("jobtasks")
@TableName("job_tasks")
public class JobTasks {

    @TableId("id")
    private String id;

    private String jobName;

    private String jobGroup;

    private String jobTime;

    private String status;

    private Date createTime;
}
