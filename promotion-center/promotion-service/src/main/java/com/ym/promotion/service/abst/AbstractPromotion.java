package com.ym.promotion.service.abst;


import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.service.IPromotionService;

/**
 * @author qushutao
 * @since 2026-07-05 22:15
 **/
public abstract class AbstractPromotion {

    private final IPromotionService promotionService;

    public AbstractPromotion(IPromotionService promotionService) {
        this.promotionService = promotionService;
    }
    public <T extends PromotionBaseDto> Long savePromotion(T promotionBaseDto) {
        // 保存活动
        Long promotionId = promotionService.savePromotion(promotionBaseDto);

        return doSavePromotion(promotionId, promotionBaseDto);
    }

    protected abstract  <T extends PromotionBaseDto> Long doSavePromotion(Long promotionId ,T promotionBaseDto);
}