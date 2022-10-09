package com.bonjour.practice.common.enums;

/**
 * 注册方式枚举类
 * @authur tc
 * @date 2022/10/9 15:18
 */
public enum RegisterTypeEnum {

    账号密码("1", "账号密码"),
    手机验证码("2", "手机验证码"),
    邮箱("3", "邮箱");

    private String key;

    private String desc;

    RegisterTypeEnum(String s, String desc) {
        this.key = s;
        this.desc = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getdesc() {
        return this.desc;
    }
}
