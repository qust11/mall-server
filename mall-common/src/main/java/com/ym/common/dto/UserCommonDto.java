package com.ym.common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-17 14:31
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCommonDto {
    private Long id;

    private String userName;

    private String phone;
}
