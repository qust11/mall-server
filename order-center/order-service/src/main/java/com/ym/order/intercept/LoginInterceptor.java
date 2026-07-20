package com.ym.order.intercept;


import com.ym.common.anotation.NoLogin;
import com.ym.common.constant.AuthConstant;
import com.ym.common.util.UserHolderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

/**
 *
 * @author qushutao
 * @since 2026-07-14 16:06
 **/
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只拦截Controller方法，过滤静态资源
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 1. 判断类上是否标注 @NoLogin
        boolean classNoLogin = handlerMethod.getBeanType().isAnnotationPresent(NoLogin.class);
        // 2. 判断方法上是否标注 @NoLogin
        boolean methodNoLogin = handlerMethod.getMethod().isAnnotationPresent(NoLogin.class);

        Long userId = getUserId(request);
        if (null != userId){
            UserHolderUtil.set(userId);
        }

        // 类/方法任意一处加了注解，直接放行
        if (classNoLogin || methodNoLogin) {
            return true;
        }

        if (null == userId) {
            // 未登录，返回JSON提示
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":401,\"msg\":\"请先登录\",\"data\":null}");
            writer.flush();
            writer.close();
            return false;
        }
        // 此处补充token校验逻辑（Redis/JWT解析等）
        return true;
    }

    private static Long getUserId(HttpServletRequest request) {
        //             根据header名称取值（不区分大小写）
        String userIdStr = request.getHeader(AuthConstant.AUTH_USER_ID_HEADER);
        if (StringUtils.isBlank( userIdStr)){
            return null;
        }
        return Long.valueOf(userIdStr);
    }
}