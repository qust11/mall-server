package com.ym.common.util;


/**
 * @author qushutao
 * @since 2026-07-01 22:20
 **/
public class UserHolderUtil {

    public static final String USER_ID = "userId";

    public static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();

    public static void set(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    public static Long get() {
        return USER_ID_HOLDER.get();
    }

    public static void clear() {
        USER_ID_HOLDER.remove();
    }
}
