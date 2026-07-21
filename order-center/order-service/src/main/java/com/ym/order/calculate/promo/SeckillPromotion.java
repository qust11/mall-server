package com.ym.order.calculate.promo;


import com.ym.order.bo.OrderBO;
import com.ym.order.constant.OrderPromotionEnum;
import com.ym.promotion.dto.PromotionDetailDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 * @author qushutao
 * @since 2026-07-19 22:24
 **/
@Component
@NoArgsConstructor
public class SeckillPromotion implements Promotion {

    @Override
    public void apply(OrderBO order, PromotionDetailDto promotionDetailDto) {

    }

    @Override
    public int getSort() {
        return 0;
    }


    public OrderPromotionEnum getPromotionEnum() {
        return OrderPromotionEnum.SECKILL;
    }
}
