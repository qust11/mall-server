package com.ym.cart.filter;


import com.ym.common.constant.AuthConstant;
import com.ym.common.util.UserHolderUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;

import java.io.IOException;

/**
 * @author qushutao
 * @since 2026-07-01 22:24
 **/
@Order(1)
@WebFilter(filterName = "loginFilter", urlPatterns = "/*") // 拦截所有请求
public class CustomFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        try {
            // 1. 解析token，拿到用户
            // 强转为HttpServletRequest
            HttpServletRequest httpReq = (HttpServletRequest) req;
            // 根据header名称取值（不区分大小写）
            UserHolderUtil.set(Long.valueOf(httpReq.getHeader(AuthConstant.AUTH_USER_ID_HEADER)));
            filterChain.doFilter(req, resp); // 放行走后面mvc、controller
        } finally {
            UserHolderUtil.clear();
        }
    }
}
