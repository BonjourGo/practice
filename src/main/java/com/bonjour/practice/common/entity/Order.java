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
@TableName("order_info")
public class Order {

    @TableId("id")
    @ApiModelProperty("订单id")
    private String orderId;

    @TableField("user_id")
    @ApiModelProperty("下单人id")
    private String userId;

    @TableField("order_status")
    @ApiModelProperty("订单状态0 未支付 1 已支付 2 已取消 3 已过期 4 已退款")
    private String orderStatus;

    @TableField("product_id")
    @ApiModelProperty("产品id")
    private Long productId;

    @TableField("number")
    @ApiModelProperty("数量")
    private Integer number;

    @TableField("pay_money")
    @ApiModelProperty("支付金额")
    private BigDecimal payMoney;

    @TableField("create_time")
    @ApiModelProperty("订单创建时间")
    private String createTime;

    @TableField("pay_time")
    @ApiModelProperty("订单支付时间")
    private String payTime;

    @TableField("cancel_time")
    @ApiModelProperty("订单取消时间")
    private String cancelTime;
}
