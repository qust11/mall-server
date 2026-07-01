package com.ym.user.dto.login.response;


import com.ym.common.dto.UserCommonDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-17 21:57
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginUserResp {

    private String token;

    private UserCommonDto user;
}
