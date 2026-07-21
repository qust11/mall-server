package com.ym.promotion.service.user;


import com.ym.promotion.dto.PromotionDetailDto;

import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-20 18:33
 **/
public interface ClientPromotionService {

    void getPromotionByUser(PromotionDetailDto promotionDetailDto, List<Long> skuIds);
}
