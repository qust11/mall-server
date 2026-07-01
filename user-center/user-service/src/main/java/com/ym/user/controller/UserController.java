package com.ym.user.controller;


import com.ym.common.result.Result;
import com.ym.user.dto.user.PwdResetReq;
import com.ym.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qushutao
 * @since 2026-06-17 18:10
 **/
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {


    private final IUserService userService;

    @PostMapping("/reset_pwd")
    public Result<String> resetPwd(@RequestBody PwdResetReq pwdResetReq) {
        userService.resetPassword(pwdResetReq);
        return Result.success("重置密码成功");
    }

}
