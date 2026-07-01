package com.ym.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ym.user.dto.UserRoleDto;
import com.ym.user.entity.User;

import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-06-17 11:32
 **/
public interface UserMapper extends BaseMapper<User> {

    List<UserRoleDto> getUserRole(Long userId);
}
