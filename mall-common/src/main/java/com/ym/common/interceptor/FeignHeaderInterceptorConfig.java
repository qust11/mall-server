package com.ym.common.interceptor;


import com.ym.common.util.UserHolderUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author qushutao
 * @since 2026-07-14 16:16
 **/
@Component
public class FeignHeaderInterceptorConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Long userId = UserHolderUtil.get();
        if (null == userId) {
            // 获取当前线程的request上下文
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                return;
            }
            ServletRequestAttributes servletAttr = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletAttr.getRequest();
            // 读取gateway传递过来的用户ID头
            String userIdStr = request.getHeader("X-User-Id");
            if (StringUtils.isNotBlank(userIdStr)) {
                userId = Long.valueOf(userIdStr);
            }
        }

        if (null != userId) {
            // feign调用时带上该header
            template.header("X-User-Id", userId.toString());
        }
    }
}
