package com.ym.user.filter;


/**
 * @author qushutao
 * @since 2026-06-17 18:21
 **/

import com.ym.common.dto.UserCommonDto;
import com.ym.common.util.JwtUtil;
import com.ym.common.util.UserContextUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class SecurityTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";
    private static final String HEADER_USER_ID = "X-User-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        UserCommonDto loginUser = null;
        try {
            // 1. 优先读取网关透传用户ID
            String userIdStr = request.getHeader(HEADER_USER_ID);
            if (StringUtils.hasText(userIdStr)) {
                loginUser = new UserCommonDto();
                loginUser.setId(Long.valueOf(userIdStr));
            } else {
                // 本地解析token
                String bearerToken = request.getHeader(AUTHORIZATION);
                if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_HEADER)) {
                    String token = bearerToken.substring(TOKEN_HEADER.length());
                    Long userId = JwtUtil.getUserIdFromToken(token);
                    loginUser = new UserCommonDto();
                    loginUser.setId(userId);
                }
            }

            // 2 填充Security上下文 + ThreadLocal
            if (loginUser != null) {
                UserContextUtil.setUser(loginUser);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        loginUser, null, Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("token解析异常", e);
            filterChain.doFilter(request, response);
        } finally {
            // 必清，防止线程池复用串号
            UserContextUtil.clear();
            SecurityContextHolder.clearContext();
        }
    }
}
