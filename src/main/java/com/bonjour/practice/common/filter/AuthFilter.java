package com.bonjour.practice.common.filter;

import com.bonjour.practice.common.entity.User;
import com.bonjour.practice.common.utils.CommonUtils;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.common.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @authur tc
 * @date 2023/4/13 9:45
 */
//@Component
public class AuthFilter implements Filter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        System.out.println(req.getRequestURI());
        // 对白名单过滤 无需授权
        if (req.getRequestURI().contains("login")
                || req.getRequestURI().contains("swagger")
                || req.getRequestURI().contains("api-docs")
                || req.getRequestURI().contains("/hello")
                || req.getRequestURI().contains("/common")) {
            filterChain.doFilter(req, resp);
            return;
        }
        String authorization = req.getHeader("Authorization");
        if (StringUtils.isBlank(authorization)) {
            throw new RuntimeException("非法操作！");
        }
        String token = (String) redisUtil.getCacheObject(authorization);
        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("请登录");
        }
        User user = CommonUtils.stringToBean(token, User.class);
        try {
            UserUtil.setUser(user);
            filterChain.doFilter(req, resp);
        } finally {
            UserUtil.removeUser();
        }
    }
}
