package com.ym.user.filter;


import com.ym.common.util.RedisUtil;
import com.ym.user.constant.RedisKeyConstants;
import com.ym.user.dto.UserDto;
import com.ym.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 *
 * @author qushutao
 * @since 2026-06-17 11:25
 **/
@Component
@AllArgsConstructor
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    private final IUserService userService; // 自定义的用户信息服务（根据手机号查用户）
    private final RedisUtil redisUtil; // Redis工具类（存储验证码）
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authToken = (SmsCodeAuthenticationToken) authentication;
        String phone = authToken.getPhone();
        String smsCode = (String) authToken.getCredentials();
        String redisKey = RedisKeyConstants.SMS_CODE_KEY + phone;
        String realCode = (String) redisUtil.get(redisKey);
        if (realCode == null) {
            throw new BadCredentialsException("验证码已过期，请重新获取");
        }
        if (!realCode.equals(smsCode)) {
            throw new BadCredentialsException("验证码错误");
        }
        // 根据手机号查询用户（唯一key，解决用户名重复）
        UserDto userDto = userService.loadUserByUsername(phone);
        if (userDto == null) {
            throw new BadCredentialsException("该手机号未注册");
        }
        // 验证通过删除验证码，防止重复使用
        redisUtil.del(redisKey);
        // 返回已认证token
        return SmsCodeAuthenticationToken.authenticated(phone, realCode, userDto.getId(), userDto, userDto.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 只处理短信登录Token
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
