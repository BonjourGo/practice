package com.bonjour.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("user")
@ApiModel("用户表")
public class User {

    @TableId("id")
    private String id;

    @TableField("nick_name")
    private String nickName;

    @TableField("phone")
    private String phone;

    @TableField("register_type")
    @ApiModelProperty("注册方式 1 密码 2 手机验证码 3 邮箱")
    private String registerType;

    @TableField("password")
    @ApiModelProperty("密码")
    private String password;

    @TableField("sex")
    private String sex;

    @TableField("status")
    private String status;
}
