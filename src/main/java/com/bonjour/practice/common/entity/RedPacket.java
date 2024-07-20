package com.bonjour.practice.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @authur tc
 * @date 2023/2/8 14:32
 */
@Data
@ApiModel("red_packet")
@TableName("red_packet")
public class RedPacket {

    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty("主键")
    private String id;

    @TableField("desc")
    @ApiModelProperty("描述")
    private String desc;

    @TableField("number")
    @ApiModelProperty("总数")
    private Integer number;

    @TableField("total_money")
    @ApiModelProperty("总金额")
    private BigDecimal totalMoney;

    @TableField("last_money")
    @ApiModelProperty("剩余金额")
    private BigDecimal lastMoney;

    @TableField("type")
    @ApiModelProperty("红包类型 1 拼运气 2 平均 3 定向")
    private String type;

    @TableField("status")
    @ApiModelProperty("status")
    private String status;

    @TableField("send_time")
    @ApiModelProperty("sendTime")
    private Date sendTime;

    @TableField("user_id")
    @ApiModelProperty("userId")
    private String userId;

    @TableField(exist = false)
    @ApiModelProperty("list")
    private List<String> ids;
}
