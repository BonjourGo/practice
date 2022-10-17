package com.bonjour.practice.common.utils;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie工具类
 */
@Service
public class CookieUtil {

    /**
     * 获取cookie的value
     * @param request
     * @param token
     * @return
     */
    public String getCookieValue(HttpServletRequest request, String token) {
        String cookieValue = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (token.equals(cookie)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 添加cookie
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
