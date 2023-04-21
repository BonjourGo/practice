package com.bonjour.practice.common.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @authur tc
 * @date 2023/2/8 14:27
 */
@Data
@ApiModel("账户")
public class UserAccount {

    private String id;

    private String userId;

    private String status;

    private BigDecimal balance;

    private String cardNumber;

    private Date creatTime;
}
