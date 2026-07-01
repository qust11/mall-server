package com.ym.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ym.common.dto.UserCommonDto;
import com.ym.user.dto.UserDto;
import com.ym.user.dto.user.PwdResetReq;
import com.ym.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author qushutao
 * @since 2026-06-17 9:12
 **/
public interface IUserService extends IService<User>, UserDetailsService {
    User selectByUsernameAndPhone(String username, String phone);

    UserDto loadUserByUsername(String phone);

    UserCommonDto loadUserByPhone(String phone);

    void resetPassword(PwdResetReq pwdResetReq);

}
