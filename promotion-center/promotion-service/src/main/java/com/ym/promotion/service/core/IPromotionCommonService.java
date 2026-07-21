package com.ym.promotion.service.core;


import com.ym.promotion.dto.PromotionBaseDto;

/**
 * @author qushutao
 * @since 2026-07-05 22:13
 **/
public interface IPromotionCommonService {

    <T extends PromotionBaseDto> Long savePromotionInfo(T promotionBaseDto);

    <T extends PromotionBaseDto> void updatePromotionInfo(Long id, T promotionBaseDto);

    void deletePromoInfo(Long promotionId);
}
