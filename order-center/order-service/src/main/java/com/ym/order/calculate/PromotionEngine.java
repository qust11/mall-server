package com.ym.order.calculate;


import com.ym.order.bo.OrderBO;
import com.ym.order.calculate.promo.IPromotionService;
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

    private final List<IPromotionService> IPromotionServices;

    public void applyPromotions(OrderBO order,PromotionDetailDto promotionDetailDto) {
        // 不改动注入的共享集合,避免并发请求排序竞态
        IPromotionServices.stream().sorted(Comparator.comparingInt(IPromotionService::getSort))
                .forEach(IPromotionServiceProcess -> IPromotionServiceProcess.apply(order,promotionDetailDto));
    }
}
