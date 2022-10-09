package com.bonjour.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @authur tc
 * @date 2022/10/9 15:45
 */
@Data
@ApiModel("产品表")
@TableName("product")
public class Product {

    @TableId("id")
    @ApiModelProperty("主键")
    private Long id;

    @TableField("name")
    @ApiModelProperty("名称")
    private String name;

    @TableField("desc")
    @ApiModelProperty("描述")
    private String desc;

    @TableField("stock")
    @ApiModelProperty("库存")
    private Integer stock;

    @TableField("init_price")
    @ApiModelProperty("初始价格")
    private BigDecimal initPrice;

    @TableField("final_price")
    @ApiModelProperty("实际价格")
    private BigDecimal finalPrice;

    @TableField("product_status")
    @ApiModelProperty("产品状态")
    private String productStatus;
}
