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
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 手机号+密码登录过滤器
 *
 * @author qushutao
 * @since 2026-06-17
 **/
public class PhonePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    //    private static final PathPatternRequestMatcher PWD_LOGIN_MATCHER =
//            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/passport/login");
//    // 同时匹配 /passport/login、/passport/register 两个POST接口
    private static final OrRequestMatcher LOGIN_REGISTER_MATCHER = new OrRequestMatcher(
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/passport/login"),
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/admin/login")
    );

    public PhonePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(LOGIN_REGISTER_MATCHER);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        Map<String, String> params = JSON.parseObject(body, Map.class);
        String phone = params.get("phone");
        String password = params.get("password");

        if (phone == null || password == null) {
            throw new IllegalArgumentException("手机号、密码不能为空");
        }
        PhonePasswordAuthenticationToken token = PhonePasswordAuthenticationToken.unauthenticated(phone, password, null);
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        // 1. 从认证Token中获取已存储的UserDto
        PhonePasswordAuthenticationToken authToken = (PhonePasswordAuthenticationToken) authResult;
        UserCommonDto userDto = authToken.getUserCommonDto();
        // 2. 直接生成JWT，无需查库
        String jwt = JwtUtil.generateTokenByUserDto(userDto);
        LoginUserResp loginResp = new LoginUserResp(jwt, userDto);
        Result<LoginUserResp> result = Result.success(loginResp);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Result<String> result = Result.fail(401, failed.getMessage());
        response.getWriter().write(JSON.toJSONString(result));
    }
}