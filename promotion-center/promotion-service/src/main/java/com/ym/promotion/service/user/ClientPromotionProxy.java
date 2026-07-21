package com.ym.promotion.service.user;


import com.ym.promotion.dto.PromotionDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-20 19:48
 **/
@Service
@RequiredArgsConstructor
public class ClientPromotionProxy {

    private final List<ClientPromotionService> userPromotionServiceList;

    public PromotionDetailDto getPromotionByUser( List<Long> skuIds) {
        PromotionDetailDto promotionDetailDto = new PromotionDetailDto();
        userPromotionServiceList.forEach(c -> c.getPromotionByUser(promotionDetailDto, skuIds));
        return promotionDetailDto;
    }
}
