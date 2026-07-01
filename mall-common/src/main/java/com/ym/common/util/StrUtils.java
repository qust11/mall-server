package com.ym.common.util;


/**
 * @author qushutao
 * @since 2026-06-30 9:34
 **/
public class StrUtils {
    public static String getSixCode(Long num) {
        // %06d：0代表补零，6总长度，d数字
        return String.format("%06d", num);
    }
}
