package com.bonjour.practice.common.utils;

import com.bonjour.practice.common.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserUtil {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    private UserUtil() {}

    // 放入user到Threadlocal
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
        return user;
    }

    public static void removeUser() {
        if (userThreadLocal.get() != null) {
            userThreadLocal.remove();
        }
    }
}
