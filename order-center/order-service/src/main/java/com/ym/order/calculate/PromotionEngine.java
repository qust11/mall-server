package com.ym.order.calculate;


import com.ym.order.bo.OrderBO;
import com.ym.order.calculate.promo.Promotion;
import com.ym.promotion.api.PromotionApi;
import com.ym.promotion.dto.PromotionDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author qushutao
 * @since 2026-07-19 22:07
 **/
@Component
@RequiredArgsConstructor
public class PromotionEngine {

    private final PromotionApi promotionApi;

    private final List<Promotion> promotions;

    public  void applyPromotions(OrderBO order) {

        List<Long> skuIds = order.getItemList().stream().map(c->c.getSkuId()).toList();
        PromotionDetailDto promotionDetailDto = promotionApi.getUserAllPromotion(order.getItemList().stream().map(OrderBO.OrderSkuBO::getSkuId).collect(Collectors.toList()));
        for (Promotion promotionProcess : promotions) {
            promotionProcess.apply(order);
        }
        promotions.sort(Comparator.comparingInt(Promotion::getSort));
        // 顺序应用
        for (Promotion p : promotions) {

        }
    }
}
