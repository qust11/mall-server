package com.ym.user.filter;

import com.ym.common.util.JwtUtil;
import com.ym.user.dto.UserDto;
import com.ym.user.service.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *
 * @author qushutao
 * @since 2026-06-16 22:20
 **/
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // 自己封装的JWT工具类
    private final IUserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 获取Authorization请求头
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String phone = null;

        // 2. 判断是否存在Bearer Token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 截取Bearer后面的token
            try {
                phone = jwtUtil.getPhoneByToken(token);
            } catch (Exception e) {
                // token过期/非法，直接放行，上下文无认证，后续返回403
            }
        }

        // 3. token有效，且当前上下文无认证信息
        if (phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDto userDto = userService.loadUserByUsername(phone);
            // 校验token是否有效
            if (jwtUtil.validateToken(token, userDto.getPhone())) {
                // 封装认证对象
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDto, null, userDto.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 存入安全上下文，标记为已认证
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}