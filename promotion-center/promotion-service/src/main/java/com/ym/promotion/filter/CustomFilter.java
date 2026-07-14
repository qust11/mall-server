package com.ym.promotion.filter;


import cn.hutool.core.util.RandomUtil;
import com.ym.common.util.UserHolderUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
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
//            String userIdStr = httpReq.getHeader(AuthConstant.AUTH_USER_ID_HEADER);
//            if (StringUtils.isBlank( userIdStr)){
//                throw new BusinessException(ResultCodeEnum.NOT_LOGIN);
//            }
//            UserHolderUtil.set(Long.valueOf(userIdStr));
            long l = RandomUtil.randomLong(100000, 100000000010000L);
            UserHolderUtil.set(l);
            filterChain.doFilter(req, resp); // 放行走后面mvc、controller
        } finally {
            UserHolderUtil.clear();
        }
    }
}
