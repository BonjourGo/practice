package com.bonjour.practice.common.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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

    @ExcelIgnore
    @TableId("id")
    @ApiModelProperty("主键")
    private Long id;

    @ExcelProperty("名称")
    @TableField("name")
    @ApiModelProperty("名称")
    private String name;

    @ExcelProperty("描述")
    @TableField("description")
    @ApiModelProperty("描述")
    private String description;

    @ExcelIgnore
    @TableField("stock")
    @ApiModelProperty("库存")
    private Integer stock;

    @ExcelIgnore
    @TableField("init_price")
    @ApiModelProperty("初始价格")
    private BigDecimal initPrice;

    @ExcelIgnore
    @TableField("final_price")
    @ApiModelProperty("实际价格")
    private BigDecimal finalPrice;

    @ExcelIgnore
    @TableField("product_status")
    @ApiModelProperty("产品状态 0 下架 1 正常 2 抢购 3 售罄")
    private String productStatus;


}
