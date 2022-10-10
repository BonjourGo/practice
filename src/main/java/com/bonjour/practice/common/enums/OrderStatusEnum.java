package com.bonjour.practice.common.enums;

/**
 * 订单状态枚举类
 * @authur tc
 * @date 2022/10/10 15:19
 */
public enum OrderStatusEnum {

//    0 未支付 1 已支付 2 已取消 3 已过期 4 已退款

    未支付("0", "未支付"),
    已支付("1", "已支付"),
    已取消("2", "已取消"),
    已过期("3", "已过期"),
    已退款("4", "已退款");

    private String key;

    private String desc;

    OrderStatusEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }

}
