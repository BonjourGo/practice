package com.bonjour.practice.common.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("订单")
public class Order {

    private String OrderId;

    private String userId;

    // 0 未支付 1 已支付 2 已取消 3 已过期
    private String orderStatus;

    private String productId;

    private BigDecimal payMoney;

    private String createTime;

    private String payTime;
}
