package com.ym.user.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author qushutao
 * @since 2026-06-17 12:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    //    用户id
    private Long userId;

    //    角色id
    private Long roleId;
}
