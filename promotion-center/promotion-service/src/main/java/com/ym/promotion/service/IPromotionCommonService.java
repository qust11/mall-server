package com.ym.promotion.service;


import com.ym.promotion.dto.PromotionBaseDto;

/**
 * @author qushutao
 * @since 2026-07-05 22:13
 **/
public interface IPromotionCommonService{

    <T extends PromotionBaseDto> Long savePromotionInfo(Long promotionId, T promotionBaseDto);

}
