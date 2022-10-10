package com.bonjour.practice.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.bonjour.practice.common.enums.RegisterTypeEnum;
import lombok.extern.slf4j.Slf4j;


/*
通用工具类
 */
@Slf4j
public class CommonUtils {

    /**
     * 格式化时间到秒
     *
     * @return
     */
    public static String getTimeString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = format.format(new Date());
        return date;
    }

    public static String getTimeStringNormal(String pattern) {
        String date = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            date = format.format(new Date());
        } catch (Exception e) {
            log.error("转换时间错误 + --" + e);
        }
        return date;
    }

    /**
     * bean to String
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * String to bean
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    public static void main(String[] args) {
        String date = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        System.out.println(date);
        System.out.println(RegisterTypeEnum.账号密码.getKey());
        System.out.println(RegisterTypeEnum.账号密码.getdesc());
    }

}
