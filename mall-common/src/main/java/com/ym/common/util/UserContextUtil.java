package com.ym.common.util;


import com.ym.common.dto.UserCommonDto;

/**
 * @author qushutao
 * @since 2026-06-17 18:13
 **/
public class UserContextUtil {
    private static final ThreadLocal<UserCommonDto> USER_CONTEXT = new ThreadLocal<>();

    public static void setUser(UserCommonDto user) {
        USER_CONTEXT.set(user);
    }

    public static UserCommonDto getUser() {
        return USER_CONTEXT.get();
    }

    public static Long getUserId() {
        return USER_CONTEXT.get().getId();
    }

    public static void clear() {
        USER_CONTEXT.remove();
    }
}
