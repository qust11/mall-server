package com.ym.user.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-17 18:11
 **/
@AllArgsConstructor
@Data
@NoArgsConstructor
public class PwdResetReq {

    private String password;
}
