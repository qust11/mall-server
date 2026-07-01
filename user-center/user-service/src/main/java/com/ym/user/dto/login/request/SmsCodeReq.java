package com.ym.user.dto.login.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-17 17:21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsCodeReq{

    private String phone;
}
