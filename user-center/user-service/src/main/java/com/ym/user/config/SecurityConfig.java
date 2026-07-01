package com.ym.user.config;


import com.ym.common.util.JwtUtil;
import com.ym.user.filter.*;
import com.ym.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * Spring Security 核心配置
 * <p>
 * 支持两种登录方式：
 * 1. 手机号+密码登录：POST /passport/pwd/login
 * 2. 手机号+验证码登录：POST /passport/sms/login
 * <p>
 * 接口鉴权统一由 JwtTokenFilter 处理
 *
 * @author qushutao
 * @since 2026-06-16 16:23
 **/
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final IUserService userService;
    private final SmsCodeAuthenticationProvider smsCodeAuthProvider;
    private final PhonePasswordAuthenticationProvider phonePwdAuthProvider;

    // 认证管理器：使用自定义的两套 Provider（替代默认的 DaoAuthenticationProvider）
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(smsCodeAuthProvider, phonePwdAuthProvider));
    }

    // JWT 全局鉴权过滤器
    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtUtil, userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationManager authenticationManager) throws Exception {
        // 创建登录过滤器（非 @Bean，只在 filterChain 内使用）
        PhonePasswordAuthenticationFilter pwdFilter =
                new PhonePasswordAuthenticationFilter(authenticationManager);
        SmsCodeAuthenticationFilter smsFilter =
                new SmsCodeAuthenticationFilter(authenticationManager);
        // 插入到账号密码认证过滤器之前，优先执行token解析
        // 实例化token过滤器
        SecurityTokenFilter tokenFilter = new SecurityTokenFilter();
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        http
                // 关闭csrf（前后端分离无session）
                .csrf(csrf -> csrf.disable())
                // 不使用Session，无会话
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 关闭基础认证、表单登录
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                // 路由权限规则
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/passport/**").permitAll() // 登录、注册接口放行，不用token
                        .anyRequest().authenticated() // 其余接口必须携带有效JWT
                );
        // 过滤器顺序（按执行顺序排列）：
        // 1. 手机号+密码登录过滤器  → POST /passport/pwd/login
        // 2. 短信验证码登录过滤器  → POST /passport/sms/login
        // 3. JWT 全局鉴权过滤器    → 除 /passport/** 外的所有请求
        http.addFilterBefore(pwdFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(smsFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}