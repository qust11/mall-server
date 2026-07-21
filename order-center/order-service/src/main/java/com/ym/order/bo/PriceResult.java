package com.ym.order.bo;


import com.ym.common.OrderPriceCalculator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-20 10:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceResult {

    private BigDecimal originalTotal;      // 商品原价总和
    private BigDecimal seckillDiscount;    // 秒杀优惠
    private BigDecimal groupBuyDiscount;   // 拼团优惠
    private BigDecimal multiPieceDiscount; // 多件多折优惠
    private BigDecimal fullReduction;      // 满减优惠
    private BigDecimal couponDiscount;     // 优惠券优惠
    private BigDecimal pointDeduction;     // 积分抵扣
    private BigDecimal shippingFee;        // 运费
    private BigDecimal finalPayAmount;     // 最终支付金额

    // 使用的优惠券
    private OrderPriceCalculator.Coupon usedCoupon;
    // 各商品明细
    private List<OrderPriceCalculator.PriceResult.ItemPriceDetail> itemDetails;

    public static class ItemPriceDetail {
        public Long skuId;
        public String skuName;
        public Integer quantity;
        public BigDecimal originalUnitPrice;
        public BigDecimal finalUnitPrice;
        public BigDecimal itemSubtotal;
        public String appliedActivity; // 应用的活动描述
    }

}
