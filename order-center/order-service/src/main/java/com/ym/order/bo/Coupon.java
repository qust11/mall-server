package com.ym.order.bo;


import com.ym.common.OrderPriceCalculator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author qushutao
 * @since 2026-07-20 10:28
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Coupon {
    public enum Type {CASH, PERCENT, FREE_SHIPPING}

    private Long couponId;
    private String couponName;
    private OrderPriceCalculator.Coupon.Type type;
    private long threshold;    // 使用门槛
    private long value;        // 面值/折扣率
    private BigDecimal maxDiscount;  // 最大优惠金额（百分比券用）
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
