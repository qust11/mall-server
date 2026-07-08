package com.ym.common.enums;


import lombok.Getter;

/**
 * @author qushutao
 * @since 2026-07-03 16:29
 * 1"优惠券
 * 2“秒杀
 * 3“满减
 * 4“多件多折
 * 5“无优惠
 **/
@Getter
public enum PromotionTypeEnum {

    COUPON(1, "优惠券"),
    SECKILL(2, "秒杀"),
    FULL_DISCOUNT(3, "满减"),
    MANY_DISCOUNT(4, "多件多折"),
    NONE(5, "无优惠");

    private final Integer code;
    private final String desc;

    PromotionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
