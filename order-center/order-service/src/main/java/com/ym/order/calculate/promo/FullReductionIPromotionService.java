package com.ym.order.calculate.promo;


import com.ym.common.bo.CartDiscountInfoBO;
import com.ym.order.bo.OrderBO;
import com.ym.order.util.DiscountAllocateUtil;
import com.ym.order.constant.OrderPromotionEnum;
import com.ym.promotion.dto.FullReductionDto;
import com.ym.promotion.dto.PromotionDetailDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 全局满减:门槛按整单原价合计判断,取优惠力度最大的一档,
 * 优惠金额按各行小计比例分摊到商品,并记录行级/整单级优惠明细
 *
 * @author qushutao
 * @since 2026-07-20 10:27
 **/
@Service
public class FullReductionIPromotionService implements IPromotionService {

    /**
     * 折扣类型 1:满减
     */
    private static final int DISCOUNT_TYPE_REDUCE = 1;

    @Override
    public void apply(OrderBO order, PromotionDetailDto promotionDetailDto) {
        List<FullReductionDto> fullReductionList = null == promotionDetailDto ? null : promotionDetailDto.getFullReductionList();
        if (CollectionUtils.isEmpty(fullReductionList) || CollectionUtils.isEmpty(order.getItemList())) {
            return;
        }

        // 门槛按整单原价判断,选择优惠力度最大的一档
        FullReductionDto best = null;
        long maxDiscount = 0L;
        for (FullReductionDto reduction : fullReductionList) {
            long discount = calcDiscount(reduction, order.getTotalPrice());
            if (discount > maxDiscount) {
                maxDiscount = discount;
                best = reduction;
            }
        }
        if (null == best || maxDiscount <= 0) {
            return;
        }

        // 优惠金额按商品行小计比例分摊,尾差由金额最大的行吸收
        List<OrderBO.OrderSkuBO> itemList = order.getItemList();
        List<Long> weights = itemList.stream().map(OrderBO.OrderSkuBO::getFinalPrice).toList();
        List<Long> shares = DiscountAllocateUtil.allocate(maxDiscount, weights);

        boolean rateType = !Integer.valueOf(DISCOUNT_TYPE_REDUCE).equals(best.getDiscountType());
        String typeDesc = rateType ? "满折" : "满减";
        int promotionType = rateType ? CartDiscountInfoBO.TYPE_FULL_RATE : CartDiscountInfoBO.TYPE_FULL_REDUCTION;
        long actualDiscount = 0L;
        for (int i = 0; i < itemList.size(); i++) {
            OrderBO.OrderSkuBO item = itemList.get(i);
            long share = shares.get(i);
            if (share <= 0) {
                continue;
            }
            actualDiscount += share;
            item.setDiscountPrice(item.getDiscountPrice() + share);
            item.setFinalPrice(item.getFinalPrice() - share);
            item.addItemDiscountInfo(buildInfo(promotionType, typeDesc, best.getId(), best.getPromotionName(), best.getPromotionDesc(), share));
        }
        if (actualDiscount <= 0) {
            return;
        }
        order.addOrderDiscountInfo(buildInfo(promotionType, typeDesc, best.getId(), best.getPromotionName(), best.getPromotionDesc(), actualDiscount));
        refreshOrderAmount(order);
    }

    /**
     * 计算单档满减/满折的优惠金额,不满足门槛或配置异常返回 0
     */
    private long calcDiscount(FullReductionDto reduction, Long baseAmount) {
        if (null == baseAmount || null == reduction.getUseThreshold() || baseAmount < reduction.getUseThreshold()) {
            return 0L;
        }
        if (Integer.valueOf(DISCOUNT_TYPE_REDUCE).equals(reduction.getDiscountType())) {
            return nullToZero(reduction.getReduceAmount());
        }
        // 满折:discountRate=90 表示 9 折,优惠 = 原价 × (100 - 折扣) / 100
        if (null != reduction.getDiscountRate()) {
            return BigDecimal.valueOf(baseAmount)
                    .multiply(BigDecimal.valueOf(100L - reduction.getDiscountRate()))
                    .divide(BigDecimal.valueOf(100L), 0, RoundingMode.HALF_UP)
                    .longValue();
        }
        return 0L;
    }

    /**
     * 订单级金额以商品行实际分摊结果为准回算,保证各行加总与整单一致
     */
    private void refreshOrderAmount(OrderBO order) {
        long discountPrice = order.getItemList().stream().mapToLong(OrderBO.OrderSkuBO::getDiscountPrice).sum();
        order.setDiscountPrice(discountPrice);
        order.setFinalPrice(order.getTotalPrice() - discountPrice);
    }

    private CartDiscountInfoBO buildInfo(int promotionType, String typeDesc, Long promotionId, String promotionName, String promotionDesc, long discountAmount) {
        CartDiscountInfoBO info = new CartDiscountInfoBO();
        info.setPromotionType(promotionType);
        info.setTypeDesc(typeDesc);
        info.setPromotionId(promotionId);
        info.setPromotionName(promotionName);
        info.setPromotionDesc(promotionDesc);
        info.setDiscountAmount(discountAmount);
        return info;
    }

    private long nullToZero(Long value) {
        return null == value ? 0L : value;
    }

    @Override
    public int getSort() {
        return 0;
    }

    @Override
    public OrderPromotionEnum getPromotionEnum() {
        return OrderPromotionEnum.FULL_REDUCTION;
    }
}
