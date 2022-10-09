package com.bonjour.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("订单")
@TableName("order")
public class Order {

    @TableId("order_id")
    @ApiModelProperty("订单id")
    private String OrderId;

    @TableField("user_id")
    @ApiModelProperty("下单人id")
    private String userId;

    // 0 未支付 1 已支付 2 已取消 3 已过期 4 已退款
    private String orderStatus;

    private String productId;

    private BigDecimal payMoney;

    private String createTime;

    private String payTime;
}
