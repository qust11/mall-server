package com.ym.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.common.dto.UserCommonDto;
import com.ym.common.util.UserContextUtil;
import com.ym.user.converter.UserConverterMapper;
import com.ym.user.dto.UserDto;
import com.ym.user.dto.UserRoleDto;
import com.ym.user.dto.user.PwdResetReq;
import com.ym.user.entity.User;
import com.ym.user.mapper.UserMapper;
import com.ym.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author qushutao
 * @since 2026-06-17 9:13
 **/
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService{

    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDto loadUserByUsername(String phone) throws UsernameNotFoundException {
        User one = Optional.ofNullable(getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone))).orElseThrow();
        UserDto userDto = UserConverterMapper.INSTANCE.toUserDto(one);
        List<UserRoleDto> userRoleList = baseMapper.getUserRole(one.getId());
        userDto.setUserRoleList(userRoleList);
        return userDto;
    }

    @Override
    public UserCommonDto loadUserByPhone(String phone) {
        User one = Optional.ofNullable(getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone))).orElseThrow();
        UserCommonDto userCommonDto = UserConverterMapper.INSTANCE.toUserCommonDto(one);
        return userCommonDto;
    }

    @Override
    public void resetPassword(PwdResetReq pwdResetReq) {
        Long userId = UserContextUtil.getUserId();
        User user = getById(userId);
        String password = passwordEncoder.encode(pwdResetReq.getPassword());
        update(new LambdaUpdateWrapper<User>().set(User::getPassword, password).eq(User::getId, userId));
    }

    @Override
    public User selectByUsernameAndPhone(String username, String phone) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
    }
}
