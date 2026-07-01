package com.ym.user.util;


import com.ym.common.util.RedisUtil;
import com.ym.user.constant.RedisKeyConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author qushutao
 * @since 2026-06-17 9:31
 **/
@Component
@AllArgsConstructor
public class SmsUtil {
    private final RedisUtil redisUtil;

    // 生成6位随机验证码
    public String sendSmsCode(String phone) {
        // 1. 生成6位随机验证码
        String smsCode = String.valueOf(new Random().nextInt(899999) + 100000);
        // 2. 存入Redis（5分钟过期）
        String cacheKey = RedisKeyConstants.SMS_CODE_KEY + phone;
        redisUtil.set(cacheKey, smsCode, 5, TimeUnit.MINUTES);
        // 3. 调用短信服务商发送验证码（实际项目需替换为真实短信接口）
        return smsCode;
    }
}
