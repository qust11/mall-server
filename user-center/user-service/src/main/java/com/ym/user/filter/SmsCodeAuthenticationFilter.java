package com.ym.user.filter;


import com.alibaba.fastjson2.JSON;
import com.ym.common.dto.UserCommonDto;
import com.ym.common.result.Result;
import com.ym.common.util.JwtUtil;
import com.ym.user.dto.login.response.LoginUserResp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 短信验证码登录过滤器
 *
 * @author qushutao
 * @since 2026-06-17 14:56
 **/
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private static final PathPatternRequestMatcher SMS_LOGIN_MATCHER =
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/passport/sms/login");

    public SmsCodeAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(SMS_LOGIN_MATCHER);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        // 读取请求体
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        Map<String, String> params = JSON.parseObject(body, Map.class);
        String phone = params.get("phone");
        String code = params.get("code");

        if (phone == null || code == null) {
            throw new IllegalArgumentException("手机号、验证码不能为空");
        }
        SmsCodeAuthenticationToken token = SmsCodeAuthenticationToken.unauthenticated(phone, code, null, null);
        return getAuthenticationManager().authenticate(token);
    }

    // 登录成功 返回JWT
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        SmsCodeAuthenticationToken token = (SmsCodeAuthenticationToken) authResult;
        UserCommonDto userCommonDto = token.getUserCommonDto();
        String jwt = JwtUtil.generateTokenByUserDto(userCommonDto);
        LoginUserResp loginResp = new LoginUserResp(jwt, userCommonDto);
        Result<LoginUserResp> result = Result.success(loginResp);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }

    // 登录失败统一返回
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Result<String> result = Result.fail(401, failed.getMessage());
        response.getWriter().write(JSON.toJSONString(result));
    }
}