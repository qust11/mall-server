package com.ym.order.calculate.promo;


import com.ym.order.bo.OrderBO;
import com.ym.order.constant.OrderPromotionEnum;
import com.ym.promotion.dto.FullReductionDto;
import com.ym.promotion.dto.PromotionDetailDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author qushutao
 * @since 2026-07-20 10:27
 **/
@AllArgsConstructor
@NoArgsConstructor
public class FullReductionPromotion implements Promotion{

    @Override
    public void apply(OrderBO order, PromotionDetailDto promotionDetailDto) {
        // 满减只处理全局满减 目前没有其他满减
        List<OrderBO.OrderSkuBO> itemList = order.getItemList();
        List<FullReductionDto> fullReductionList = promotionDetailDto.getFullReductionList();
        // 仅处理全局满减，按门槛降序
        List<FullReductionDto> sorted = fullReductionList.stream()
                .sorted(Comparator.comparing(FullReductionDto::getUseThreshold).reversed())
                .collect(Collectors.toList());
        Long maxDiscount = 0L;
        for (FullReductionDto reduction : sorted) {
            if (order.getFinalPrice().compareTo(reduction.getUseThreshold()) < 0) {
                continue;
            }
            Integer type = reduction.getDiscountType();
            if (type == 1) {
                // 满减
                Long actual = reduction.getReduceAmount();
                maxDiscount = Math.max(maxDiscount, actual);
            } else {
                // 满折
                Long discountRate = reduction.getDiscountRate();
                Long actual = BigDecimal.valueOf((100 - discountRate)).multiply(BigDecimal.valueOf(order.getFinalPrice())).longValue();
                maxDiscount = Math.max(maxDiscount, actual);
            }
        }
        order.setFinalPrice(order.getFinalPrice() - maxDiscount);
        order.setDiscountPrice(null != order.getDiscountPrice() ? order.getDiscountPrice() + maxDiscount : maxDiscount);
    }

    @Override
    public int getSort() {
        return 0;
    }

    @Override
    public OrderPromotionEnum getPromotionEnum() {
        return OrderPromotionEnum.COUPON;
    }


    public boolean isValid() {
        return true;
    }

}
