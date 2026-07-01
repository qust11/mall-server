package com.ym.user.dto;


import com.ym.common.dto.UserCommonDto;
import com.ym.user.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-06-17 13:28
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto extends UserCommonDto implements UserDetails {

    private String password;

    private List<UserRoleDto> userRoleList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(userRoleList)) {
            return List.of(new SimpleGrantedAuthority(CommonConstant.DEFAULT_ROLE));
        }
        return userRoleList.stream().map(userRoleDto -> new SimpleGrantedAuthority(userRoleDto.getRoleName())).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getPhone();
    }
}
