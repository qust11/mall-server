package com.ym.user.filter;

import com.ym.user.dto.UserDto;
import com.ym.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 手机号+密码登录认证 Provider
 *
 * @author qushutao
 * @since 2026-06-17
 **/
@Component
@AllArgsConstructor
public class PhonePasswordAuthenticationProvider implements AuthenticationProvider {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PhonePasswordAuthenticationToken authToken = (PhonePasswordAuthenticationToken) authentication;
        String phone = authToken.getPhone();
        String password = authToken.getPassword();

        // 1. 根据手机号查用户
        UserDto userDetails = userService.loadUserByUsername(phone);
        if (userDetails == null) {
            throw new BadCredentialsException("该手机号未注册");
        }

        // 2. 比对密码
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        // 3. 返回已认证 Token
        return PhonePasswordAuthenticationToken.authenticated(phone, password, userDetails, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PhonePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}