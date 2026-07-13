package com.ym.promotion.service.abst;


import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.service.IPromotionService;

/**
 * @author qushutao
 * @since 2026-07-05 22:15
 **/
public abstract class AbstractPromotion<T extends PromotionBaseDto> {

    private final IPromotionService promotionService;

    public AbstractPromotion(IPromotionService promotionService) {
        this.promotionService = promotionService;
    }

    public Long savePromotion(T promotionBaseDto) {
        // 保存活动
        Long promotionId = promotionService.savePromotion(promotionBaseDto);

        return doSavePromotion(promotionId, promotionBaseDto);
    }

    protected abstract Long doSavePromotion(Long promotionId, T promotionBaseDto);


}