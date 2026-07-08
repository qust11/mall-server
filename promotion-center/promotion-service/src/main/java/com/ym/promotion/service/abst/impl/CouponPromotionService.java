package com.ym.promotion.service.abst.impl;


import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.service.IPromotionCommonService;
import com.ym.promotion.service.IPromotionService;
import com.ym.promotion.service.abst.AbstractPromotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author qushutao
 * @since 2026-07-06 8:04
 **/
@Service
public class CouponPromotionService extends AbstractPromotion {
    private final IPromotionCommonService couponService;

    public CouponPromotionService(@Autowired IPromotionService promotionService, @Qualifier("couponServiceImpl") IPromotionCommonService couponService) {
        super(promotionService);
        this.couponService = couponService;
    }
    @Override
    protected <T extends PromotionBaseDto> Long doSavePromotion(Long promotionId, T promotionBaseDto) {
        return couponService.savePromotionInfo(promotionId, promotionBaseDto);
    }
}
