package com.ym.user.filter;


import com.ym.common.dto.UserCommonDto;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author qushutao
 * @since 2026-06-17 9:43
 **/
@Getter
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

    private String phone;
    private String code;
    private Long id;
    private UserCommonDto userCommonDto;

    public SmsCodeAuthenticationToken(String phone, String code, Long id, UserCommonDto userCommonDto) {
        super(null);
        this.phone = phone;
        this.code = code;
        this.id = id;
        this.userCommonDto = userCommonDto;
        this.setAuthenticated(false);
    }

    public SmsCodeAuthenticationToken(String phone, String code, Long id, UserCommonDto userCommonDto, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = phone;
        this.code = code;
        this.id = id;
        this.userCommonDto = userCommonDto;
        super.setAuthenticated(true);
    }

    public static SmsCodeAuthenticationToken unauthenticated(String phone, String code, Long id, UserCommonDto userDto) {
        return new SmsCodeAuthenticationToken(phone, code, id, userDto);
    }

    public static SmsCodeAuthenticationToken authenticated(String phone, String code, Long id, UserCommonDto userDto, Collection<? extends GrantedAuthority> authorities) {
        return new SmsCodeAuthenticationToken(phone, code, id, userDto, authorities);
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }
}
