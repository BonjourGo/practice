package com.bonjour.practice.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @authur tc
 * @date 2024/7/10 10:34
 */
public class celuemoshi {
    public static void main(String[] args) {
        Method method = ClassChoice.getMethod("1");
        String name = method.method("1");
        System.out.println(name);
    }
}


interface Method {
    String method(String type);
}

class Method01 implements Method {
    @Override
    public String method(String type) {
        return "method01" + "=" + type;
    }
}

class Method02 implements Method {
    @Override
    public String method(String type) {
        return "method02" + "=" + type;
    }
}

class ClassChoice {
    private static final Map<String, Method> map = new ConcurrentHashMap<>();
    static {
        map.put("1", new Method01());
        map.put("2", new Method02());
    }
    public static Method getMethod(String type) {
        return map.get(type);
    }
}