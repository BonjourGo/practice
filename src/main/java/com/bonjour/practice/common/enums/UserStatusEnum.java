package com.bonjour.practice.common.enums;

/**
 * 账户状态枚举类
 * @authur tc
 * @date 2022/10/9 15:29
 */
public enum UserStatusEnum {

    正常("1", "正常"),
    锁定("2", "锁定"),
    注销("2", "注销");

    private String key;

    private String desc;

    UserStatusEnum(String key, String desc) {
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
