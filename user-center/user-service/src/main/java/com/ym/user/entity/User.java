package com.ym.user.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author qushutao
 * @since 2026-06-17 11:26
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_info")
public class User {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    // 用户姓名
    private String username;

    // 用户密码
    private String password;

    // 手机号
    private String phone;

    // 地址
    private String address;
}
