package com.ym.promotion.service.abst.impl;


import com.ym.promotion.dto.req.SeckillReq;
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
public class SeckillPromotionService extends AbstractPromotion<SeckillReq> {
    private final IPromotionCommonService couponService;

    public SeckillPromotionService(@Autowired IPromotionService promotionService, @Qualifier("seckillServiceImpl") IPromotionCommonService couponService) {
        super(promotionService);
        this.couponService = couponService;
    }

    @Override
    protected Long doSavePromotion(Long promotionId, SeckillReq promotionBaseDto) {
        return couponService.savePromotionInfo(promotionId, promotionBaseDto);
    }
}
