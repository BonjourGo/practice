package com.bonjour.practice.common.config;

import com.bonjour.practice.common.interceptor.LimiterInterceptor;
import com.bonjour.practice.common.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @authur tc
 * @date 2022/12/28 16:07
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;

    @Autowired
    private LimiterInterceptor limiterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limiterInterceptor)
//                .addPathPatterns("/hello/**")
                .addPathPatterns("/order/**")
//                .excludePathPatterns("/swagger-ui.html").excludePathPatterns()
        ;
//        registry.addInterceptor(logInterceptor).excludePathPatterns("/login");
    }
}
