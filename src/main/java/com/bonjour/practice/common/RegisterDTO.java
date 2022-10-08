package com.bonjour.practice.common;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @authur tc
 * @date 2022/10/8 13:50
 */
@Data
@ApiModel("注册dto")
public class RegisterDTO {

    @TableField("nike_name")
    private String nikeName;

    @TableField("phone")
    private String phone;

    @ApiModelProperty("注册方式 1 密码 2 手机验证码 3 邮箱")
    private String registerType;

    @ApiModelProperty("密码")
    private String password;
}
