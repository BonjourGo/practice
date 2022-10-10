package com.bonjour.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @authur tc
 * @date 2022/10/10 15:39
 */
@Data
@ApiModel("订单详情表")
@TableName("order_detail")
public class OrderDetail {

    private String id;

    private String orderId;

    private String productId;

    private Integer number;

    private String orderDetailStatus;

    private String cancelTime;

    private BigDecimal finalPrice;
}
