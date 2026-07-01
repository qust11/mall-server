package com.ym.user.controller;

import com.ym.common.result.Result;
import com.ym.user.dto.login.request.SmsCodeReq;
import com.ym.user.util.SmsUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author qushutao
 * @since 2026-06-16 16:52
 **/
@RestController
@RequestMapping("/passport")
@AllArgsConstructor
public class PassportController {
    private final SmsUtil smsUtil; // 自定义的短信发送工具类

    // 发送验证码
    @PostMapping("/send_sms")
    public Result<String> sendSmsCode(@RequestBody SmsCodeReq smsCodeReq) {
        String smsCode = smsUtil.sendSmsCode(smsCodeReq.getPhone());
        return Result.success("验证码发送成功:" + smsCode);
    }

}
