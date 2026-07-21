package com.ym.order.calculate.promo;


import com.ym.order.bo.OrderBO;
import com.ym.order.constant.OrderPromotionEnum;
import com.ym.promotion.dto.PromotionDetailDto;

/**
 *
 * @author qushutao
 * @since 2026-07-19 22:09
 **/
public interface Promotion {

    void apply(OrderBO order, PromotionDetailDto promotionDetailDto);

    int getSort();

    OrderPromotionEnum getPromotionEnum();
}
