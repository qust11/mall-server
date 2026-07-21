package com.ym.order.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-20 9:51
 **/
@Getter
@AllArgsConstructor
public enum OrderPromotionEnum {

    SECKILL("秒杀"),
    GROUP_BUY("拼团"),
    MULTIPLE_DISCOUNT("多件多折"),
    DISCOUNT("折扣"),
    COUPON("优惠券"),
    ;
    private final String desc;


    public List<OrderPromotionEnum> normalPromotionList() {
        return Arrays.asList(SECKILL, GROUP_BUY, MULTIPLE_DISCOUNT, DISCOUNT, COUPON);
    }
}
