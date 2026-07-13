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

    COUPON(1, "优惠券", "couponServiceImpl"),
    SECKILL(2, "秒杀","seckillServiceImpl"),
    FULL_DISCOUNT(3, "满减","fullReductionServiceImpl"),
    MANY_DISCOUNT(4, "多件多折","ManyDiscountServiceImpl"),
    NONE(5, "无优惠","NonePromotionServiceImpl");

    private final Integer code;
    private final String desc;
    private final String serviceName;

    PromotionTypeEnum(Integer code, String desc, String serviceName) {
        this.code = code;
        this.desc = desc;
        this.serviceName = serviceName;
    }

    public static PromotionTypeEnum getByCode(Integer code) {
        for (PromotionTypeEnum value : PromotionTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
