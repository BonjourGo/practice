package com.bonjour.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("user")
@ApiModel("用户表")
public class User {

    @TableId("id")
    private String id;

    @TableField("nike_name")
    private String nikeName;

    @TableField("phone")
    private String phone;

    @TableField("sex")
    private String sex;

    @TableField("status")
    private String status;
}
