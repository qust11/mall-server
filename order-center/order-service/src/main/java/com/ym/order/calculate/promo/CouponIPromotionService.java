package com.ym.order.calculate.promo;


import com.ym.common.bo.CartDiscountInfoBO;
import com.ym.common.enums.CouponRangeTypeEnum;
import com.ym.order.bo.OrderBO;
import com.ym.order.util.DiscountAllocateUtil;
import com.ym.order.constant.OrderPromotionEnum;
import com.ym.promotion.dto.CouponDto;
import com.ym.promotion.dto.PromotionDetailDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券:门槛按参与商品范围的行小计(已扣除满减等先序优惠)判断,
 * 自动选取优惠力度最大的一张券,券优惠按参与商品行小计比例分摊,并记录行级/整单级优惠明细
 *
 * @author qushutao
 * @since 2026-07-20 10:27
 **/
@Service
public class CouponIPromotionService implements IPromotionService {

    @Override
    public void apply(OrderBO order, PromotionDetailDto promotionDetailDto) {
        List<CouponDto> couponList = null == promotionDetailDto ? null : promotionDetailDto.getCouponList();
        if (CollectionUtils.isEmpty(couponList) || CollectionUtils.isEmpty(order.getItemList())) {
            return;
        }

        // 遍历全部可用券,取优惠力度最大的一张(连同其参与商品范围)
        CouponDto bestCoupon = null;
        long bestDiscount = 0L;
        List<OrderBO.OrderSkuBO> bestItems = List.of();
        for (CouponDto couponDto : couponList) {
            if (!isValid(couponDto.getStartTime(), couponDto.getEndTime(), LocalDateTime.now())) {
                continue;
            }
            List<OrderBO.OrderSkuBO> participants = matchItems(couponDto, order.getItemList());
            if (CollectionUtils.isEmpty(participants)) {
                continue;
            }
            long totalPrice = participants.stream().mapToLong(OrderBO.OrderSkuBO::getFinalPrice).sum();
            if (totalPrice < nullToZero(couponDto.getCouponThreshold())) {
                continue;
            }
            // 券面额不能超过参与商品金额
            long discount = Math.min(nullToZero(couponDto.getCouponAmount()), totalPrice);
            if (discount > bestDiscount) {
                bestDiscount = discount;
                bestCoupon = couponDto;
                bestItems = participants;
            }
        }
        if (null == bestCoupon || bestDiscount <= 0) {
            return;
        }

        // 券优惠按参与商品行小计比例分摊,尾差由金额最大的行吸收
        List<Long> weights = bestItems.stream().map(OrderBO.OrderSkuBO::getFinalPrice).toList();
        List<Long> shares = DiscountAllocateUtil.allocate(bestDiscount, weights);

        long actualDiscount = 0L;
        for (int i = 0; i < bestItems.size(); i++) {
            OrderBO.OrderSkuBO item = bestItems.get(i);
            long share = shares.get(i);
            if (share <= 0) {
                continue;
            }
            actualDiscount += share;
            item.setDiscountPrice(item.getDiscountPrice() + share);
            item.setFinalPrice(item.getFinalPrice() - share);
            item.addItemDiscountInfo(buildInfo(bestCoupon, share));
        }
        if (actualDiscount <= 0) {
            return;
        }
        order.addOrderDiscountInfo(buildInfo(bestCoupon, actualDiscount));
        refreshOrderAmount(order);
    }

    /**
     * 按券范围圈定参与商品:全平台/全店为全部,指定类目/商品按行属性匹配
     */
    private List<OrderBO.OrderSkuBO> matchItems(CouponDto couponDto, List<OrderBO.OrderSkuBO> itemList) {
        Integer rangeType = couponDto.getRangeType();
        if (CouponRangeTypeEnum.ALL_PLANTFORM.getCode().equals(rangeType) || CouponRangeTypeEnum.ALL_STORE.getCode().equals(rangeType)) {
            return itemList;
        }
        if (CouponRangeTypeEnum.SPECIFIC_CATEGORY.getCode().equals(rangeType)) {
            List<Long> categoryIds = couponDto.getCategoryIds();
            if (CollectionUtils.isEmpty(categoryIds)) {
                return List.of();
            }
            return itemList.stream().filter(item -> null != item.getCategoryId() && categoryIds.contains(item.getCategoryId())).toList();
        }
        if (CouponRangeTypeEnum.SPECIFIC_PRODUCT.getCode().equals(rangeType)) {
            List<Long> spuIds = couponDto.getSpuIds();
            if (CollectionUtils.isEmpty(spuIds)) {
                return List.of();
            }
            return itemList.stream().filter(item -> null != item.getSpuId() && spuIds.contains(item.getSpuId())).toList();
        }
        return List.of();
    }

    private boolean isValid(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime now) {
        if (null == startTime || null == endTime) {
            return false;
        }
        return !now.isBefore(startTime) && now.isBefore(endTime);
    }

    /**
     * 订单级金额以商品行实际分摊结果为准回算,保证各行加总与整单一致
     */
    private void refreshOrderAmount(OrderBO order) {
        long discountPrice = order.getItemList().stream().mapToLong(OrderBO.OrderSkuBO::getDiscountPrice).sum();
        order.setDiscountPrice(discountPrice);
        order.setFinalPrice(order.getTotalPrice() - discountPrice);
    }

    private CartDiscountInfoBO buildInfo(CouponDto couponDto, long discountAmount) {
        CartDiscountInfoBO info = new CartDiscountInfoBO();
        info.setPromotionType(CartDiscountInfoBO.TYPE_COUPON);
        info.setTypeDesc("优惠券");
        info.setPromotionId(couponDto.getId());
        info.setPromotionName(couponDto.getPromotionName());
        info.setPromotionDesc(couponDto.getPromotionDesc());
        info.setDiscountAmount(discountAmount);
        return info;
    }

    private long nullToZero(Long value) {
        return null == value ? 0L : value;
    }

    @Override
    public int getSort() {
        return 2;
    }

    @Override
    public OrderPromotionEnum getPromotionEnum() {
        return OrderPromotionEnum.COUPON;
    }
}
