package com.ym.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author qushutao
 * @since 2026-06-17 13:20
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRoleDto {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色id
     */
    private Long roleId;
}
