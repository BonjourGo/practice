package com.bonjour.practice.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @authur tc
 * @date 2024/7/5 13:57
 */
@Slf4j
//@Component
public class StaticFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // 判断是否需要进行过滤
        String uri = httpServletRequest.getRequestURI();
        try {
            if (uri.contains("addProduct")) {
                log.info(uri);
                long s = System.currentTimeMillis();
                filterChain.doFilter(servletRequest, servletResponse);
                long s1 = System.currentTimeMillis();
                long use = (s1 - s) / 1000;
                log.info("use : {}s", use);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (Exception e) {

        }
    }
}
