package com.bonjour.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @authur tc
 * @date 2023/2/8 14:32
 */
@Data
@ApiModel("red_packet")
@TableName("red_packet")
public class RedPacket {

    @TableId("id")
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

    @TableField("")
    @ApiModelProperty("")
    private String status;

    @TableField("")
    @ApiModelProperty("")
    private Date sendTime;

    @TableField("")
    @ApiModelProperty("")
    private String user_id;
}
