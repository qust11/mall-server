package com.ym.order.calculate.promo;


import com.ym.common.enums.CouponRangeTypeEnum;
import com.ym.order.bo.OrderBO;
import com.ym.order.constant.OrderPromotionEnum;
import com.ym.promotion.dto.CouponDto;
import com.ym.promotion.dto.PromotionDetailDto;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author qushutao
 * @since 2026-07-20 10:27
 **/
@AllArgsConstructor
@Service
public class CouponPromotion implements Promotion {

    @Override
    public void apply(OrderBO order, PromotionDetailDto promotionDetailDto) {
        List<CouponDto> couponList = promotionDetailDto.getCouponList();
        if (CollectionUtils.isEmpty(couponList)) {
            return;
        }
        List<OrderBO.OrderSkuBO> itemList = order.getItemList();

        Long maxDiscount = 0L;
        Long couponId = null;

        List<OrderBO.OrderSkuBO> updateItem = new ArrayList<>();
        for (CouponDto couponDto : couponList) {
            boolean valid = isValid(couponDto.getStartTime(), couponDto.getEndTime(), LocalDateTime.now());
            if (!valid) {
                break;
            }

            Integer rangeType = couponDto.getRangeType();
            Long couponThreshold = couponDto.getCouponThreshold();

            if (CouponRangeTypeEnum.ALL_PLANTFORM.getCode().equals(rangeType)) {
                updateItem = order.getItemList();
                if (order.getFinalPrice() >= couponThreshold) {
                    couponId = null;
                    maxDiscount = Long.max(maxDiscount, couponDto.getCouponAmount());
                }
            } else if (CouponRangeTypeEnum.SPECIFIC_CATEGORY.getCode().equals(rangeType)) {
                List<Long> categoryIds = couponDto.getCategoryIds();
                updateItem = itemList.stream().filter(c -> categoryIds.contains(c.getCategoryId())).toList();
                Long totalPrice = updateItem.stream().mapToLong(OrderBO.OrderSkuBO::getFinalPrice).sum();
                if (totalPrice >= couponThreshold) {
                    maxDiscount = Long.max(maxDiscount, couponDto.getCouponAmount());
                    couponId = couponDto.getId();
                }
            } else if (CouponRangeTypeEnum.SPECIFIC_PRODUCT.getCode().equals(rangeType)) {
                List<Long> spuIds = couponDto.getSpuIds();
                updateItem = itemList.stream().filter(c -> spuIds.contains(c.getSpuId())).toList();
                Long totalPrice = updateItem.stream().mapToLong(OrderBO.OrderSkuBO::getFinalPrice).sum();
                if (totalPrice >= couponThreshold) {
                    maxDiscount = Long.max(maxDiscount, couponDto.getCouponAmount());
                    couponId = couponDto.getId();
                }
            }
        }
        // 反向跟新所有的参与优惠的商品价格
        Map<Long, CouponDto> couponMap = couponList.stream().collect(Collectors.toMap(CouponDto::getId, Function.identity()));
        CouponDto couponDto = couponMap.get(couponId);
        if (null == couponDto) {
            return;
        }
        Long couponAmount = couponDto.getCouponAmount();
        Long totalPrice = updateItem.stream().mapToLong(c -> c.getQuantity() * c.getFinalPrice()).sum();
        BigDecimal rate = BigDecimal.valueOf(couponAmount).divide(BigDecimal.valueOf(totalPrice).setScale(2, RoundingMode.HALF_UP));
        List<OrderBO.OrderSkuBO> itemSorted = updateItem.stream().sorted(Comparator.comparing(OrderBO.OrderSkuBO::getFinalPrice).reversed()).toList();

        Map<Long, OrderBO.OrderSkuBO> skuBoMap = itemList.stream().collect(Collectors.toMap(OrderBO.OrderSkuBO::getSkuId, Function.identity()));

        for (OrderBO.OrderSkuBO orderSkuBO : itemSorted) {
            Long currentPrice = orderSkuBO.getQuantity() * orderSkuBO.getFinalPrice();
            Long discountPrice = rate.multiply(BigDecimal.valueOf(currentPrice).setScale(2, RoundingMode.HALF_DOWN)).longValue();
            OrderBO.OrderSkuBO orignSku = skuBoMap.get(orderSkuBO.getSkuId());
            orignSku.setDiscountPrice(orignSku.getDiscountPrice() + discountPrice);
            orignSku.setFinalPrice(orignSku.getFinalPrice() - discountPrice);
        }
    }

    @Override
    public int getSort() {
        return 2;
    }

    @Override
    public OrderPromotionEnum getPromotionEnum() {
        return OrderPromotionEnum.COUPON;
    }


    public boolean isValid(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime now) {
        return now.isAfter(startTime) && now.isBefore(endTime);
    }
}
