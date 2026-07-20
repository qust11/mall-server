package com.ym.order.calculate;


import com.ym.order.bo.OrderBO;
import com.ym.order.calculate.promo.Promotion;

import java.util.Comparator;
import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-19 22:07
 **/
public class PromotionEngine {
    public static void applyPromotions(OrderBO order, List<Promotion> promotions) {
        promotions.sort(Comparator.comparingInt(Promotion::getSort));
        // 顺序应用
        for (Promotion p : promotions) {
            p.apply(order);
        }
    }
}
