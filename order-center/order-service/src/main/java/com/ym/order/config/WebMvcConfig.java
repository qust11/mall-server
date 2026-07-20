package com.ym.order.config;


import com.ym.order.intercept.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author qushutao
 * @since 2026-07-14 16:42
 **/
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor headerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerInterceptor)
                .addPathPatterns("/**");
    }
}
