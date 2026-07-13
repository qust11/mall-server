package com.ym.common.enums;


import lombok.Getter;

/**
 *
 * @author qushutao
 * @since 2026-07-13 21:41
 * 状态 1:未使用  2:已使用 3:已过期 4:已作废
 **/
@Getter
public enum PromotionCouponReceivedStateEnum {
    NOT_USED(1, "未使用"),
    USED(2, "已使用"),
    EXPIRED(3, "已过期"),
    CANCELED(4, "已作废");
    private final Integer code;
    private final String msg;

    PromotionCouponReceivedStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
