package com.bonjour.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @authur tc
 * @date 2022/10/18 17:09
 */
@Builder
@Data
@ApiModel("地址")
@TableName("address")
public class Address {

    @TableId("id")
    @ApiModelProperty("主键")
    private String id;

    @TableField("receive_name")
    @ApiModelProperty("收货人名字")
    private String receiveName;

    @TableField("receive_phone")
    @ApiModelProperty("收货人手机号码")
    private String receivePhone;

    @TableField("province")
    @ApiModelProperty("省")
    private String province;

    @TableField("city")
    @ApiModelProperty("市")
    private String city;

    @TableField("county")
    @ApiModelProperty("区县")
    private String county;

    @TableField("address")
    @ApiModelProperty("详细地址")
    private String address;

    @TableField("mark")
    @ApiModelProperty("备注")
    private String mark;

    @TableField("status")
    @ApiModelProperty("地址状态")
    private String status;

    @TableField("add_time")
    @ApiModelProperty("添加时间")
    private Date addTime;
}
