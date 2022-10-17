package com.bonjour.practice.common.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录dto")
public class LoginDTO {

    @TableField("nick_name")
    private String nickName;

    @TableField("phone")
    private String phone;

    @ApiModelProperty("注册方式 1 密码 2 手机验证码 3 邮箱")
    private String registerType;

    @ApiModelProperty("密码")
    private String password;
}
