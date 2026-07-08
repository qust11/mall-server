package com.ym.promotion.converter;


import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.resp.PromotionListResp;
import com.ym.promotion.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-25 22:50
 **/
@Mapper
public interface PromotionConverter {

    PromotionConverter INSTANCE = Mappers.getMapper(PromotionConverter.class);
    Promotion toPromotion(PromotionBaseDto brands);


    PromotionListResp toPromotionResp(Promotion promotion);

    List<PromotionListResp> batchToPromotionRespList(List<Promotion> promotions);
}
