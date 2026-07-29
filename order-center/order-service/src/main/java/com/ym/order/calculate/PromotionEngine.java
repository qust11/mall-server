package com.ym.order.calculate;


import com.ym.order.bo.OrderBO;
import com.ym.order.calculate.promo.Promotion;
import com.ym.promotion.dto.PromotionDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-19 22:07
 **/
@Component
@RequiredArgsConstructor
public class PromotionEngine {

    private final List<Promotion> promotions;

    public void applyPromotions(OrderBO order,PromotionDetailDto promotionDetailDto) {
        promotions.sort(Comparator.comparingInt(Promotion::getSort));
        for (Promotion promotionProcess : promotions) {
            promotionProcess.apply(order,promotionDetailDto);
        }
    }
}
