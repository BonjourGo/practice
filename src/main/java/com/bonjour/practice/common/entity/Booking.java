package com.bonjour.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @authur tc
 * @date 2023/6/25 10:15
 */
@Data
@ApiModel("预约实体类")
@TableName("booking")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @TableId("id")
    @ApiModelProperty("id")
    private String id;

    private String wdId;

    private String phone;

    private String status;

    private String bookingDate;

    private String number;

    private BigDecimal curr;

    private BigDecimal next;

    public Booking(BigDecimal curr, BigDecimal next) {
        this.curr = curr;
        this.next = next;
    }
}
