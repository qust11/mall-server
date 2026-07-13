package com.ym.promotion.service.impl;

import com.ym.promotion.converter.CouponConverter;
import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.PromotionIdBaseDto;
import com.ym.promotion.dto.req.CouponReq;
import com.ym.promotion.entity.Coupon;
import com.ym.promotion.entity.Promotion;
import com.ym.promotion.mapper.CouponMapper;
import com.ym.promotion.service.ICouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.promotion.service.IPromotionCommonService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现�?
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService, IPromotionCommonService {

    @Override
    public <T extends PromotionBaseDto> Long savePromotionInfo(Long promotionId, T promotionBaseDto) {
        CouponReq couponReq = (CouponReq) promotionBaseDto;
        Coupon coupon = CouponConverter.INSTANCE.toCoupon(couponReq);
        if (CollectionUtils.isNotEmpty(couponReq.getSpuIds())){
            coupon.setSkuIds(StringUtils.join(couponReq.getSpuIds(), ","));
        }
        if (CollectionUtils.isNotEmpty(couponReq.getCategoryIds())){
            coupon.setCategoryIds(StringUtils.join(couponReq.getCategoryIds(), ","));
        }
        coupon.setPromotionId(promotionId);
        coupon.setRemainStock(couponReq.getStock());
        this.save(coupon);
        return coupon.getId();
    }

}
