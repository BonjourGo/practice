package com.bonjour.practice.common.utils;

import com.bonjour.practice.common.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserUtil {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    private UserUtil() {}

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    public static User getUser() {
        User user = userThreadLocal.get();
        if (user == null) {
            user = new User();
            user.setId("test");
            user.setPhone("123456789");
            user.setNickName("bonjour");
        }
        return userThreadLocal.get();
    }

    public static void removeUser() {
        userThreadLocal.remove();
    }
}
