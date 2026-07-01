package com.ym.user.filter;

import com.ym.common.dto.UserCommonDto;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 手机号+密码登录认证 Token
 *
 * @author qushutao
 * @since 2026-06-17
 **/
@Getter
public class PhonePasswordAuthenticationToken extends AbstractAuthenticationToken {

    private final String phone;
    private final String password;
    private final UserCommonDto userCommonDto;

    /**
     * 未认证：仅保存手机号和密码
     */
    public PhonePasswordAuthenticationToken(String phone, String password, UserCommonDto userCommonDto) {
        super(null);
        this.phone = phone;
        this.password = password;
        this.userCommonDto = userCommonDto;
        setAuthenticated(false);
    }

    /**
     * 已认证：保存手机号+用户权限
     */
    public PhonePasswordAuthenticationToken(String phone, String password,UserCommonDto userCommonDto,
                                            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = phone;
        this.password = password;
        this.userCommonDto = userCommonDto;
        super.setAuthenticated(true);
    }

    public static PhonePasswordAuthenticationToken unauthenticated(String phone, String password, UserCommonDto userDto) {
        return new PhonePasswordAuthenticationToken(phone, password, userDto);
    }

    public static PhonePasswordAuthenticationToken authenticated(String phone, String password, UserCommonDto userDto,
                                                                  Collection<? extends GrantedAuthority> authorities) {
        return new PhonePasswordAuthenticationToken(phone, password, userDto, authorities);
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }
}