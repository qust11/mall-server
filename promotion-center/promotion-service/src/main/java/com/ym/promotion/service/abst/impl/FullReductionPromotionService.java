package com.ym.promotion.service.abst.impl;


import com.ym.promotion.dto.req.FullReductionReq;
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
public class FullReductionPromotionService extends AbstractPromotion<FullReductionReq> {
    private final IPromotionCommonService promotionCommonService;

    public FullReductionPromotionService(@Autowired IPromotionService promotionService, @Qualifier("fullReductionServiceImpl") IPromotionCommonService promotionCommonService) {
        super(promotionService);
        this.promotionCommonService = promotionCommonService;
    }
    @Override
    protected Long doSavePromotion(Long promotionId, FullReductionReq promotionBaseDto) {
        return promotionCommonService.savePromotionInfo(promotionId, promotionBaseDto);
    }

}
